import javax.swing.*;
import java.awt.*;

public class BattleshipView extends JFrame {
    private JButton[][] playerButtons;
    private JLabel turnLabel;
    private JTextArea statusArea;
    private JTextField chatBox;
    private JButton swapButton;
    private JButton placeButton;
    private JButton resetButton;
    private ImageIcon hitIcon;
    private ImageIcon missIcon;
    private ImageIcon shipHIcon;
    private ImageIcon shipVIcon;
    private JPanel mainPanel;
    private JTextArea playerShips;
    private JTextArea enemyShips;

    public BattleshipView() {
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.8);
        int height = (int) (screenSize.getHeight() * 0.8);

        setPreferredSize(new Dimension(width, height));

        // Load images
        hitIcon = new ImageIcon(BattleshipView.class.getResource("/hit.png"));
        missIcon = new ImageIcon(BattleshipView.class.getResource("/miss.png"));
        shipHIcon = new ImageIcon(BattleshipView.class.getResource("/shipH.png"));
        shipVIcon = new ImageIcon(BattleshipView.class.getResource("/shipV.png"));

        // Create main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        add(mainPanel);
        
        MainMenu menu = new MainMenu();
        mainPanel.add(menu);

        // Top panel for logo and buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 82, 10));
        topPanel.setPreferredSize(new Dimension(width, height / 10));
        topPanel.setBackground(Color.BLACK);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        topPanel.add(createButton("Start", new Dimension(81, 42)));

        resetButton = createButton("Reset", new Dimension(81, 42));
        topPanel.add(resetButton);

        ImageIcon logoImage = new ImageIcon(BattleshipView.class.getResource("/Logo.png"));
        JLabel logo = new JLabel(logoImage);
        logo.setPreferredSize(new Dimension(60, 60));
        topPanel.add(logo);

        topPanel.add(createButton("Help", new Dimension(81, 42)));

        placeButton = createButton("Place", new Dimension(81, 42));
        topPanel.add(placeButton);

        // Center panel for game grid and player stats
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setPreferredSize(new Dimension(width, height * 8 / 10));
        centerPanel.setBackground(Color.BLACK);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Game grid panel with border
        JPanel gameGridPanel = new JPanel(new GridLayout(10, 10, 0, 0));
        gameGridPanel.setPreferredSize(new Dimension(width * 3 / 4, height));
        gameGridPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));
        centerPanel.add(gameGridPanel, BorderLayout.WEST);

        playerButtons = new JButton[10][10];
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                playerButtons[row][col] = new JButton();
                playerButtons[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gameGridPanel.add(playerButtons[row][col]);
            }
        }

        // Right panel for player stats and combat log
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(width * 1 / 4, height * 8 / 10));
        rightPanel.setBackground(Color.BLACK);
        centerPanel.add(rightPanel, BorderLayout.CENTER);

        // Player stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setPreferredSize(new Dimension(width * 1 / 4, height * 3 / 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        statsPanel.setBackground(new java.awt.Color(255, 247, 231));
        rightPanel.add(statsPanel);

        turnLabel = new JLabel("Player's turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 25));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsPanel.add(turnLabel);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Your ships panel
        JPanel yourShipsPanel = new JPanel();
        yourShipsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        yourShipsPanel.setBackground(new java.awt.Color(255, 247, 231));
        playerShips = new JTextArea("Your Ships\nDestroyer: 5/5");
        playerShips.setFont(new Font("Arial", Font.BOLD, 12));
        playerShips.setEditable(false);
        playerShips.setBackground(new java.awt.Color(255, 247, 231));
        yourShipsPanel.add(playerShips);
        yourShipsPanel.add(new JLabel(shipHIcon));
        yourShipsPanel.add(new JLabel(shipVIcon));
        statsPanel.add(yourShipsPanel);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Enemy ships panel
        JPanel enemyShipsPanel = new JPanel();
        enemyShipsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        enemyShipsPanel.setBackground(new java.awt.Color(255, 247, 231));
        enemyShips = new JTextArea("Enemy Ships\nDestroyer: 5/5");
        enemyShips.setFont(new Font("Arial", Font.BOLD, 12));
        enemyShips.setEditable(false);
        enemyShips.setBackground(new java.awt.Color(255, 247, 231));
        enemyShipsPanel.add(enemyShips);
        enemyShipsPanel.add(new JLabel(shipHIcon));
        enemyShipsPanel.add(new JLabel(shipVIcon));
        statsPanel.add(enemyShipsPanel);

        // Combat log
        statusArea = new JTextArea("Initializing game boards...\nSelect where to place your ships.\nStarting combat!");
        statusArea.setFont(new Font("Arial", Font.BOLD, 12));
        statusArea.setEditable(false);
        statusArea.setBackground(new java.awt.Color(255, 150, 15));
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setPreferredSize(new Dimension(width * 1 / 4 - 20, height));
        rightPanel.add(scrollPane);

        // Text box
        chatBox = new JTextField();
        chatBox.setPreferredSize(new Dimension(width * 1 / 4 - 20, 20));
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(chatBox);

        // Bottom panel for swap button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setPreferredSize(new Dimension(width, height / 10));
        bottomPanel.setBackground(Color.BLACK);
        swapButton = new JButton("Swap");
        swapButton.setPreferredSize(new Dimension(81, 42));
        bottomPanel.add(swapButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Pack and center the frame
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String text, Dimension dimension) {
        JButton button = new JButton(text);
        button.setPreferredSize(dimension);
        return button;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton[][] getPlayerButtons() {
        return playerButtons;
    }

    public JLabel getTurnLabel() {
        return turnLabel;
    }

    public JTextArea getStatusArea() {
        return statusArea;
    }

    public JTextField getChatBox() {
        return chatBox;
    }

    public JButton getSwapButton() {
        return swapButton;
    }

    public JButton getPlaceButton() {
        return placeButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public ImageIcon getHitIcon() {
        return hitIcon;
    }

    public ImageIcon getMissIcon() {
        return missIcon;
    }

    public ImageIcon getShipHIcon() {
        return shipHIcon;
    }

    public ImageIcon getShipVIcon() {
        return shipVIcon;
    }

    public void updateStatus(String status) {
        statusArea.append("\n" + status);
        statusArea.setCaretPosition(statusArea.getDocument().getLength());
    }

    public void setTurnLabel(String text) {
        turnLabel.setText(text);
    }

    public void updatePlayerShipsStatus(int remaining) {
        playerShips.setText("Your Ships\nDestroyer: " + remaining + "/5");
    }

    public void updateEnemyShipsStatus(int remaining) {
        enemyShips.setText("Enemy Ships\nDestroyer: " + remaining + "/5");
    }

    public String showOrientationDialog() {
        Object[] options = {"Horizontal", "Vertical"};
        int n = JOptionPane.showOptionDialog(this,
                "Choose the ship orientation:",
                "Ship Orientation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return (n == JOptionPane.YES_OPTION) ? "H" : "V";
    }

    public void showGameEndDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}
