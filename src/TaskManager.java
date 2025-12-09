import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;
    private Connection conn;

    public TaskManager() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:tasks.db");
            initDatabase();
            loadTasksFromDatabase();
        } catch (SQLException e) {
            System.out.println("Database init error: " + e.getMessage());
        }
    }

    private void initDatabase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT NOT NULL," +
                "description TEXT," +
                "priority TEXT," +
                "status TEXT," +
                "assignedTo TEXT," +
                "createdAt TEXT" +
                ")";
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    private void loadTasksFromDatabase() {
        tasks.clear();
        int maxId = 0;
        String sql = "SELECT id, title, description, priority, status, assignedTo, createdAt FROM tasks";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String priority = rs.getString("priority");
                String status = rs.getString("status");
                String assignedTo = rs.getString("assignedTo");
                LocalDateTime createdAt = LocalDateTime.parse(rs.getString("createdAt"));
                Task t = new Task(id, title, description, priority, assignedTo);
                t.setStatus(status);
                tasks.add(t);
                if (id > maxId) {
                    maxId = id;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database load error: " + e.getMessage());
        }
        nextId = maxId + 1;
    }

    public synchronized Task addTask(String title, String description,
                                     String priority, String assignedTo) {
        int id = nextId++;
        Task task = new Task(id, title, description, priority, assignedTo);
        tasks.add(task);
        String sql = "INSERT INTO tasks(id, title, description, priority, status, assignedTo, createdAt) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, task.getId());
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, priority);
            ps.setString(5, task.getStatus());
            ps.setString(6, assignedTo);
            ps.setString(7, task.getCreatedAt().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database insert error: " + e.getMessage());
        }
        return task;
    }

    public synchronized boolean updateTaskStatus(int id, String newStatus) {
        Task target = null;
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setStatus(newStatus);
                target = task;
                break;
            }
        }
        if (target == null) {
            return false;
        }
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database update error: " + e.getMessage());
        }
        return true;
    }

    public synchronized boolean deleteTask(int id) {
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        if (!removed) {
            return false;
        }
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database delete error: " + e.getMessage());
        }
        return true;
    }

    public synchronized List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
}
