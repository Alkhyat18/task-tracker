import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TaskClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        new TaskClient().start();
    }

    public void start() {
        try (
                Socket socket = new Socket(HOST, PORT);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream()), true);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to Task Server.");
            System.out.println(in.readLine());

            boolean running = true;

            while (running) {
                printMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        addTask(scanner, out, in);
                        break;
                    case "2":
                        listTasks(out, in);
                        break;
                    case "3":
                        updateTask(scanner, out, in);
                        break;
                    case "4":
                        deleteTask(scanner, out, in);
                        break;
                    case "0":
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printMenu() {
        System.out.println("\n=== Task Tracker ===");
        System.out.println("1. Add Task");
        System.out.println("2. List Tasks");
        System.out.println("3. Update Task Status");
        System.out.println("4. Delete Task");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private void addTask(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Priority (LOW / MEDIUM / HIGH): ");
        String priority = scanner.nextLine();

        System.out.print("Assigned To: ");
        String assignedTo = scanner.nextLine();

        String cmd = "ADD|" + title + "|" + description + "|" + priority + "|" + assignedTo;
        out.println(cmd);
        System.out.println("Server: " + in.readLine());
    }

    private void listTasks(PrintWriter out, BufferedReader in) throws IOException {
        out.println("LIST");
        System.out.println("Server: " + in.readLine());
    }

    private void updateTask(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Task ID: ");
        String id = scanner.nextLine();

        System.out.print("New Status (TODO / DOING / DONE): ");
        String status = scanner.nextLine();

        out.println("UPDATE|" + id + "|" + status);
        System.out.println("Server: " + in.readLine());
    }

    private void deleteTask(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Task ID: ");
        String id = scanner.nextLine();

        out.println("DELETE|" + id);
        System.out.println("Server: " + in.readLine());
    }
}
