import java.time.LocalDateTime;

public class Task {
    private final int id;
    private final String title;
    private final String description;
    private final String priority;
    private String status;
    private final String assignedTo;
    private final LocalDateTime createdAt;

    public Task(int id, String title, String description,
                String priority, String assignedTo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.assignedTo = assignedTo;
        this.status = "TODO";
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title +
                " | priority=" + priority +
                " | status=" + status +
                " | assignedTo=" + assignedTo;
    }
}
