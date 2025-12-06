import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(HOST, PORT);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream()), true)
        ) {

            String line = in.readLine();
            System.out.println("Server: " + line);


            out.println("PING");
            System.out.println("\nSent: PING");
            System.out.println("Server: " + in.readLine());


            out.println("LIST");
            System.out.println("\nSent: LIST");
            System.out.println("Server: " + in.readLine());


            String addCmd = "ADD|Finish Java project|Implement UPDATE and DELETE|HIGH|mohammed";
            out.println(addCmd);
            System.out.println("\nSent: " + addCmd);
            String addResponse = in.readLine();
            System.out.println("Server: " + addResponse);


            int newTaskId = -1;
            try {
                int idx = addResponse.lastIndexOf(' ');
                if (idx != -1) {
                    String idStr = addResponse.substring(idx + 1).trim();
                    newTaskId = Integer.parseInt(idStr);
                }
            } catch (Exception e) {
                System.out.println("Could not parse new task id from response.");
            }


            out.println("LIST");
            System.out.println("\nSent: LIST");
            System.out.println("Server: " + in.readLine());

            if (newTaskId != -1) {

                String updateCmd = "UPDATE|" + newTaskId + "|DONE";
                out.println(updateCmd);
                System.out.println("\nSent: " + updateCmd);
                System.out.println("Server: " + in.readLine());


                out.println("LIST");
                System.out.println("\nSent: LIST");
                System.out.println("Server: " + in.readLine());


                String deleteCmd = "DELETE|" + newTaskId;
                out.println(deleteCmd);
                System.out.println("\nSent: " + deleteCmd);
                System.out.println("Server: " + in.readLine());


                out.println("LIST");
                System.out.println("\nSent: LIST");
                System.out.println("Server: " + in.readLine());
            } else {
                System.out.println("\nSkipping UPDATE/DELETE because task id could not be parsed.");
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

