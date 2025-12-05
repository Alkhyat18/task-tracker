import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class TaskServer {

    private static final int PORT = 5000;
    private final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        TaskServer server = new TaskServer();
        server.seedDemoTasks();
        server.start();
    }

    private void seedDemoTasks() {
        taskManager.addTask("Set up project repo",
                "Initialize Git and basic structure", "HIGH", "team");
        taskManager.addTask("Design task model",
                "Define Task and TaskManager classes", "MEDIUM", "team");

        System.out.println("Seeded demo tasks:");
        for (Task t : taskManager.getAllTasks()) {
            System.out.println("  " + t);
        }
    }

    public void start() {
        System.out.println("Starting TaskServer on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("TaskServer started. Waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());
                ClientHandler handler = new ClientHandler(clientSocket, taskManager);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket socket;
        private final TaskManager taskManager;

        public ClientHandler(Socket socket, TaskManager taskManager) {
            this.socket = socket;
            this.taskManager = taskManager;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(socket.getOutputStream()), true)
            ) {
                out.println("OK|Connected to TaskServer");

                String line;
                while ((line = in.readLine()) != null) {
                    String response = handleCommand(line);
                    out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignore) {
                }
            }
        }

        private String handleCommand(String line) {

            if (line == null || line.isBlank()) {
                return "ERROR|Empty command";
            }

            String[] parts = line.split("\\|");
            String command = parts[0].toUpperCase();

            switch (command) {
                case "PING":
                    return "OK|PONG";

                case "LIST":
                    var tasks = taskManager.getAllTasks();
                    if (tasks.isEmpty()) {
                        return "OK|No tasks yet.";
                    }
                    StringBuilder sb = new StringBuilder("OK|");
                    for (Task t : tasks) {
                        sb.append(t.toString()).append(" || ");
                    }
                    return sb.toString();

                case "ADD":
                    if (parts.length < 5) {
                        return "ERROR|Usage: ADD|title|description|priority|assignedTo";
                    }
                    String title = parts[1];
                    String description = parts[2];
                    String priority = parts[3];
                    String assignedTo = parts[4];

                    Task newTask = taskManager.addTask(title, description, priority, assignedTo);
                    return "OK|Task added with id " + newTask.getId();

                default:
                    return "ERROR|Unknown command: " + command;
            }
        }
    }
}
