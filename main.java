import javax.swing.*;
import java.awt.*;

public class main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setResizable(false);

        // Set the preferred size of the frame
        frame.setPreferredSize(new Dimension(1241, 698));
        
        // Set the background color of the content pane
        frame.getContentPane().setBackground(Color.BLACK);

        // Create the main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK); // Set the background color of the main panel
        frame.add(mainPanel);

        // Top panel for the logo and buttons
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 82, 10));
        top.setPreferredSize(new Dimension(1241, 70));
        top.setBackground(Color.BLACK); // Set the background color of the top panel
        mainPanel.add(top, BorderLayout.NORTH);

        // Add the logo
        JLabel logo = new JLabel(new ImageIcon("Logo.png")); // Replace with your logo image path
        logo.setPreferredSize(new Dimension(70, 70));
        top.add(logo);

        // Add the control buttons
        top.add(createButton("Start", new Dimension(81, 42)));
        top.add(createButton("Reset", new Dimension(81, 42)));
        
        // Add the score label
        JLabel scoreLabel = new JLabel("0 : 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setForeground(Color.WHITE); // Set the text color to white
        top.add(scoreLabel);
        
        // Add the control buttons
        top.add(createButton("Ship", new Dimension(81, 42)));
        top.add(createButton("Place", new Dimension(81, 42)));
        
        // Center panel for game grid and player stats
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setPreferredSize(new Dimension(1241, 618));
        centerPanel.setBackground(Color.BLACK); // Set the background color of the center panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Game grid panel
        JPanel gameGridPanel = new JPanel(new GridLayout(10, 10, 0, 0));
        gameGridPanel.setPreferredSize(new Dimension(541, 549));
        gameGridPanel.setBackground(Color.ORANGE);
        centerPanel.add(gameGridPanel, BorderLayout.WEST);

        // Fill the game grid with buttons
        for (int i = 0; i < 100; i++) {
            JButton button = new JButton();
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: add borders if you like
            gameGridPanel.add(button);
        }

        // Right panel for player stats and combat log
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(700, 618));
        rightPanel.setBackground(Color.BLACK); // Set the background color of the right panel
        centerPanel.add(rightPanel, BorderLayout.CENTER);

        // Player stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setPreferredSize(new Dimension(532, 345));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statsPanel.setBackground(new java.awt.Color(255, 247, 231)); // Set the background color of the stats panel
        rightPanel.add(statsPanel);

        JLabel turnLabel = new JLabel("Player's turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 25));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsPanel.add(turnLabel);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Gap created or margin-bottom: 10px

        JTextArea playerShips = new JTextArea("Your Ships\nCarrier: 5/5\nCruiser: 4/4\nDestroyer: 3/3\nMissile Frigate: 3/3\nSubmarine: 2/2");
        playerShips.setFont(new Font("Arial", Font.BOLD, 14));
        playerShips.setEditable(false);
        playerShips.setBackground(new java.awt.Color(255, 247, 231));
        statsPanel.add(playerShips);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Gap created or margin-bottom: 10px

        JTextArea enemyShips = new JTextArea("Enemy Ships\nCarrier: 5/5\nCruiser: 4/4\nDestroyer: 3/3\nMissile Frigate: 3/3\nSubmarine: 2/2");
        enemyShips.setFont(new Font("Arial", Font.BOLD, 14));
        enemyShips.setEditable(false);
        enemyShips.setBackground(new java.awt.Color(255, 247, 231));
        statsPanel.add(enemyShips);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Gap created or margin-bottom: 20px

        // Combat log area
        JTextArea combatLog = new JTextArea("Initializing game boards...\nSelect where to place your ships.\nStarting combat!\nEnemy attacked at 6,6, missing.");
        combatLog.setFont(new Font("Arial", Font.BOLD, 14));
        combatLog.setEditable(false);
        combatLog.setBackground(new java.awt.Color(255, 150, 15));
        // Display scroll pane if the text over the limit
        JScrollPane scrollPane = new JScrollPane(combatLog);
        scrollPane.setPreferredSize(new Dimension(480, 310));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(scrollPane);
        
        // Text box
        JTextField textBox = new JTextField();
        textBox.setPreferredSize(new Dimension(450, 21));
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(textBox);

        // Bottom panel for swap button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setPreferredSize(new Dimension(1241, 70));
        bottomPanel.setBackground(Color.BLACK); // Set the background color of the bottom panel
        JButton swapButton = new JButton("Swap");
        swapButton.setPreferredSize(new Dimension(81, 42));
        bottomPanel.add(swapButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Pack and set the frame visible
        frame.pack();
        frame.setVisible(true);
    }

    private static JButton createButton(String text, Dimension dimension) {
        JButton button = new JButton(text);
        button.setPreferredSize(dimension);
        return button;
    }
}
