import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    public synchronized Task addTask(String title, String description,
                                     String priority, String assignedTo) {
        Task task = new Task(nextId++, title, description, priority, assignedTo);
        tasks.add(task);
        return task;
    }

    public synchronized boolean updateTaskStatus(int id, String newStatus) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setStatus(newStatus);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteTask(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    public synchronized List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
}
