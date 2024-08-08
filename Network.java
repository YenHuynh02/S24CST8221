import java.io.*;
import java.net.*;

public class Network implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BattleshipView view;
    private Host host;

    public Network(Socket socket, Host host) {
        this.socket = socket;
        this.host = host;
        setupStreams();
    }
    
    private void setupStreams() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                if (view != null) {
                    view.updateStatus(message);
                } else if (host != null) {
                    host.broadcast(message, this);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (host != null) {
                host.removeClient(this);
            }
        }
    }
}
