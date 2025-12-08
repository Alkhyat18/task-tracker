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

    private final ObservableList<String> taskItems = FXCollections.observableArrayList();
    private TableView<String> tableView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TextField titleField = new TextField();
        TextField descriptionField = new TextField();
        TextField priorityField = new TextField();
        TextField assignedToField = new TextField();

        titleField.setPromptText("Title");
        descriptionField.setPromptText("Description");
        priorityField.setPromptText("Priority");
        assignedToField.setPromptText("Assigned To");

        tableView = new TableView<>(taskItems);
        TableColumn<String, String> col = new TableColumn<>("Tasks");
        col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        col.setPrefWidth(700);
        tableView.getColumns().add(col);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(e -> {
            String title = titleField.getText();
            if (!title.isBlank()) {
                taskItems.add(title);
                titleField.clear();
            }
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

        HBox buttons = new HBox(10, addButton, updateButton, deleteButton, refreshButton);

        BorderPane root = new BorderPane();
        root.setTop(form);
        root.setCenter(tableView);
        root.setBottom(buttons);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Task Tracker GUI");
        stage.setScene(scene);
        stage.show();
    }
}

