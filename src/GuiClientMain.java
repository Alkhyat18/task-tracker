import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GuiClientMain extends Application {

    private ServerConnection server;
    private final ObservableList<String> taskItems = FXCollections.observableArrayList();
    private TableView<String> tableView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            server = new ServerConnection("localhost", 5000);
        } catch (Exception e) {
            System.out.println("Could not connect to server");
        }

        TextField titleField = new TextField();
        TextField descriptionField = new TextField();
        TextField priorityField = new TextField();
        TextField assignedToField = new TextField();
        TextField statusField = new TextField();

        titleField.setPromptText("Title");
        descriptionField.setPromptText("Description");
        priorityField.setPromptText("Priority");
        assignedToField.setPromptText("Assigned To");
        statusField.setPromptText("Status (TODO/DOING/DONE)");

        tableView = new TableView<>(taskItems);
        TableColumn<String, String> col = new TableColumn<>("Tasks");
        col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        col.setPrefWidth(700);
        tableView.getColumns().add(col);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button refreshButton = new Button("Refresh");

        refreshButton.setOnAction(e -> refreshTasks());

        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String priority = priorityField.getText();
            String assignedTo = assignedToField.getText();

            if (title.isBlank()) {
                return;
            }

            addTask(title, description, priority, assignedTo);

            titleField.clear();
            descriptionField.clear();
            priorityField.clear();
            assignedToField.clear();
        });
        updateButton.setOnAction(e -> {
            String selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }
            String newStatus = statusField.getText().trim();
            if (newStatus.isEmpty()) {
                return;
            }
            int id = extractIdFromRow(selected);
            if (id == -1) {
                return;
            }
            updateTaskStatus(id, newStatus.toUpperCase());
            refreshTasks();
            statusField.clear();
        });

        deleteButton.setOnAction(e -> {
            String selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }
            int id = extractIdFromRow(selected);
            if (id == -1) {
                return;
            }
            deleteTask(id);
            refreshTasks();
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Title"), 0, 0);
        form.add(titleField, 1, 0);
        form.add(new Label("Description"), 0, 1);
        form.add(descriptionField, 1, 1);
        form.add(new Label("Priority"), 0, 2);
        form.add(priorityField, 1, 2);
        form.add(new Label("Assigned To"), 0, 3);
        form.add(assignedToField, 1, 3);
        form.add(new Label("Status"), 0, 4);
        form.add(statusField, 1, 4);

        HBox buttons = new HBox(10, addButton, updateButton, deleteButton, refreshButton);

        BorderPane root = new BorderPane();
        root.setTop(form);
        root.setCenter(tableView);
        root.setBottom(buttons);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Task Tracker GUI");
        stage.setScene(scene);
        stage.show();

        refreshTasks();
    }

    private void refreshTasks() {
        try {
            String response = server.send("LIST");
            taskItems.clear();
            if (response != null && response.startsWith("OK|")) {
                String body = response.substring(3);
                String[] values = body.split("\\|\\|");
                for (String v : values) {
                    String trimmed = v.trim();
                    if (!trimmed.isEmpty()) {
                        taskItems.add(trimmed);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error refreshing tasks");
        }
    }

    private void addTask(String title, String description, String priority, String assignedTo) {
        try {
            String cmd = "ADD|" + title + "|" + description + "|" + priority + "|" + assignedTo;
            server.send(cmd);
            refreshTasks();
        } catch (Exception e) {
            System.out.println("Error adding task");
        }
    }
    private void updateTaskStatus(int id, String newStatus) {
        try {
            String cmd = "UPDATE|" + id + "|" + newStatus;
            server.send(cmd);
        } catch (Exception e) {
            System.out.println("Error updating task");
        }
    }
    private void deleteTask(int id) {
        try {
            String cmd = "DELETE|" + id;
            server.send(cmd);
        } catch (Exception e) {
            System.out.println("Error deleting task");
        }
    }
    private int extractIdFromRow(String row) {
        try {
            int open = row.indexOf('[');
            int close = row.indexOf(']');
            if (open == -1 || close == -1 || close <= open + 1) {
                return -1;
            }
            String idStr = row.substring(open + 1, close).trim();
            return Integer.parseInt(idStr);
        } catch (Exception e) {
            return -1;
        }
    }
}
