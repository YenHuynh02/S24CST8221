import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client implements Runnable {
    private String serverIp;
    private int serverPort;
    private String name;
    private JTextArea statusLabel;
    private Socket socket;
    private Network network;

    public Client(String serverIp, int serverPort, String name, JTextArea jTextArea) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.name = name;
        this.statusLabel = jTextArea;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverIp, serverPort);
            network = new Network(socket, null); // Replace with your BattleshipView
            new Thread(network).start();
            statusLabel.setText("Connected to server");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    public Network getNetwork() {
        return network;
    }
}
