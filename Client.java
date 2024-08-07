import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {
    private static final String SERVER_IP = "localhost"; // Change this to the server's IP address
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private Network network;

    public Client(BattleshipView view) {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            network = new Network(socket, view);
            new Thread(network).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Network getNetwork() {
        return network;
    }
}
