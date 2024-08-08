import java.io.*;
import java.net.*;
import java.util.*;

public class Host {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private List<Network> clients;

    public Host() {
        try {
            serverSocket = new ServerSocket(PORT);
            clients = new ArrayList<>();
            System.out.println("Server started. Waiting for clients...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);
                Network network = new Network(clientSocket, this);
                clients.add(network);
                new Thread(network).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcast(String message, Network sender) {
        for (Network client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public void removeClient(Network client) {
        clients.remove(client);
    }

    public static void main(String[] args) {
        Host host = new Host();
        host.start();
    }
}