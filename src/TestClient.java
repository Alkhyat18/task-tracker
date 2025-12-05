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
            System.out.println("Server: " + in.readLine());

            out.println("PING");
            System.out.println("Sent: PING");
            System.out.println("Server: " + in.readLine());

            out.println("LIST");
            System.out.println("Sent: LIST");
            System.out.println("Server: " + in.readLine());

            String addCmd = "ADD|Finish Java project|Implement server ADD command|HIGH|mohammed";
            out.println(addCmd);
            System.out.println("Sent: " + addCmd);
            System.out.println("Server: " + in.readLine());

            out.println("LIST");
            System.out.println("Sent: LIST");
            System.out.println("Server: " + in.readLine());

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
