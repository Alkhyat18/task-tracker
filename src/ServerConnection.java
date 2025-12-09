import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ServerConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        in.readLine();
    }

    public String send(String command) throws IOException {
        out.println(command);
        return in.readLine();
    }

    public void close() throws IOException {
        socket.close();
    }
}
