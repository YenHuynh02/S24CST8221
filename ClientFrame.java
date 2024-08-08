import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends JFrame {
    private JTextField nameField;
    private JTextField portField;
    private JTextField ipField;
    private JLabel statusLabel;
    private JButton connectButton;
    private JButton cancelButton;

    public ClientFrame() {
        setTitle("Client Setup");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("IP:"));
        ipField = new JTextField("localhost"); // Default IP
        add(ipField);

        add(new JLabel("Port:"));
        portField = new JTextField("12345"); // Default port
        add(portField);

        add(new JLabel("Status:"));
        statusLabel = new JLabel("");
        add(statusLabel);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        add(connectButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(cancelButton);
    }

    private void connectToServer() {
        String name = nameField.getText();
        String ip = ipField.getText();
        int port;

        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid port number");
            return;
        }

        // Call the BattleshipMain to start the game
        BattleshipMain.startGame(ip, port, name);
        dispose(); // Close the ClientFrame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientFrame frame = new ClientFrame();
            frame.setVisible(true);
        });
    }
}
