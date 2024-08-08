import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HostFrame extends JFrame {
    private JTextField nameField;
    private JTextField portField;
    private JLabel statusLabel;
    private JButton hostButton;
    private JButton cancelButton;
    private Host host;

    public HostFrame() {
        setTitle("Host Setup");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Port:"));
        portField = new JTextField("12345"); // Default port
        add(portField);

        add(new JLabel("Status:"));
        statusLabel = new JLabel("");
        add(statusLabel);

        hostButton = new JButton("Host");
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startHosting();
            }
        });
        add(hostButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(cancelButton);
    }

    private void startHosting() {
        String name = nameField.getText();
        int port;

        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid port number");
            return;
        }

        host = new Host();
        new Thread().start();
        hostButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HostFrame frame = new HostFrame();
            frame.setVisible(true);
        });
    }
}
