import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BattleshipView {
    private JFrame frame;
    private JButton[][] gameGrid;
    private JButton swapButton;
    private JTextArea statusArea;
    private JLabel turnLabel;
    private ImageIcon hitIcon;
    private ImageIcon missIcon;

    public BattleshipView() {
        frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Integrate the menu bar
        MainMenu menuBar = new MainMenu(frame);
        frame.setJMenuBar(menuBar);

        // Get the screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.8); // Adjust the size by multiply 0.8
        int height = (int) (screenSize.getHeight() * 0.8);

        // Set the preferred size of the frame dynamically based on screen size
        frame.setPreferredSize(new Dimension(width, height));

        // Set the background color of the content pane
        frame.getContentPane().setBackground(Color.BLACK);

        // Create the main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK); // Set the background color of the main panel
        frame.add(mainPanel);

        // Top panel for the logo and buttons
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 82, 10));
        top.setPreferredSize(new Dimension(width, height / 10));
        top.setBackground(Color.BLACK); // Set the background color of the top panel
        mainPanel.add(top, BorderLayout.NORTH);

        // Add the control buttons
        top.add(createButton("Start", new Dimension(81, 42)));
        top.add(createButton("Reset", new Dimension(81, 42)));

        // Add the logo
        ImageIcon logoImage = new ImageIcon(main.class.getResource("/Logo.png"));
        JLabel logo = new JLabel(logoImage);
        logo.setPreferredSize(new Dimension(60, 60));
        top.add(logo);

        // Add the control buttons
        top.add(createButton("Help", new Dimension(81, 42)));
        top.add(createButton("Place", new Dimension(81, 42)));

        // Center panel for game grid and player stats
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setPreferredSize(new Dimension(width, height * 8 / 10));
        centerPanel.setBackground(Color.BLACK); // Set the background color of the center panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Game grid panel with border
        JPanel gameGridPanel = new JPanel(new GridLayout(10, 10, 0, 0));
        gameGridPanel.setPreferredSize(new Dimension(width * 3 / 4, height));
        gameGridPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));
        centerPanel.add(gameGridPanel, BorderLayout.WEST);

        gameGrid = new JButton[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gameGrid[i][j] = new JButton();
                gameGrid[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gameGridPanel.add(gameGrid[i][j]);
            }
        }

        // Right panel for player stats and combat log
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(width * 1 / 4, height * 8 / 10));
        rightPanel.setBackground(Color.BLACK); // Set the background color of the right panel
        centerPanel.add(rightPanel, BorderLayout.CENTER);

        // Player stats panel for text
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setPreferredSize(new Dimension(width * 1 / 4, height * 3 / 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        statsPanel.setBackground(new java.awt.Color(255, 247, 231)); // Set the background color of the stats panel
        rightPanel.add(statsPanel);

        turnLabel = new JLabel("Player's turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 25));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsPanel.add(turnLabel);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Gap created or margin-bottom: 10px

        // Create panel for "Your Ships" with image
        JPanel yourShipsPanel = new JPanel();
        yourShipsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        yourShipsPanel.setBackground(new java.awt.Color(255, 247, 231));
        JTextArea playerShips = new JTextArea("Your Ships\nDestroyer: 3/3");
        playerShips.setFont(new Font("Arial", Font.BOLD, 12));
        playerShips.setEditable(false);
        playerShips.setBackground(new java.awt.Color(255, 247, 231));
        yourShipsPanel.add(playerShips);
        yourShipsPanel.add(new JLabel(new ImageIcon(main.class.getResource("/shipH.png"))));
        yourShipsPanel.add(new JLabel(new ImageIcon(main.class.getResource("/shipV.png"))));
        statsPanel.add(yourShipsPanel);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Gap created or margin-bottom: 10px

        // Create panel for "Enemy Ships" with image
        JPanel enemyShipsPanel = new JPanel();
        enemyShipsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        enemyShipsPanel.setBackground(new java.awt.Color(255, 247, 231));
        JTextArea enemyShips = new JTextArea("Enemy Ships\nDestroyer: 3/3");
        enemyShips.setFont(new Font("Arial", Font.BOLD, 12));
        enemyShips.setEditable(false);
        enemyShips.setBackground(new java.awt.Color(255, 247, 231));
        enemyShipsPanel.add(enemyShips);
        enemyShipsPanel.add(new JLabel(new ImageIcon(main.class.getResource("/shipH.png"))));
        enemyShipsPanel.add(new JLabel(new ImageIcon(main.class.getResource("/shipV.png"))));
        statsPanel.add(enemyShipsPanel);

        // Combat log area
        statusArea = new JTextArea("\nInitializing game boards...\nSelect where to place your ships.\nStarting combat!\n");
        statusArea.setFont(new Font("Arial", Font.BOLD, 12));
        statusArea.setEditable(false);
        statusArea.setBackground(new java.awt.Color(255, 150, 15));
        // Display scroll pane if the text over the limit
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setPreferredSize(new Dimension(width * 1 / 4 - 20, height * 2 / 10));
        rightPanel.add(scrollPane);

        // Bottom panel for swap button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setPreferredSize(new Dimension(width, height / 10));
        bottomPanel.setBackground(Color.BLACK); // Set the background color of the bottom panel
        swapButton = new JButton("Swap");
        swapButton.setPreferredSize(new Dimension(81, 42));
        bottomPanel.add(swapButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Pack and center the frame on the screen
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Load images
        hitIcon = new ImageIcon(BattleshipView.class.getResource("/hit.png"));
        missIcon = new ImageIcon(BattleshipView.class.getResource("/miss.png"));
    }

    private JButton createButton(String text, Dimension dimension) {
        JButton button = new JButton(text);
        button.setPreferredSize(dimension);
        return button;
    }

    public JButton getSwapButton() {
        return swapButton;
    }

    public JButton getGameGridButton(int x, int y) {
        return gameGrid[x][y];
    }

    public void setButtonListener(ActionListener listener) {
        swapButton.addActionListener(listener);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gameGrid[i][j].addActionListener(listener);
            }
        }
    }

    public void setButtonIcon(int x, int y, char status) {
        if (status == 'H') {
            gameGrid[x][y].setIcon(hitIcon);
        } else if (status == 'M') {
            gameGrid[x][y].setIcon(missIcon);
        } else {
            gameGrid[x][y].setIcon(null);
        }
    }

    public void updateStatus(String status) {
        statusArea.append(status + "\n");
        statusArea.setCaretPosition(statusArea.getDocument().getLength());
    }

    public void updateTurnLabel(String text) {
        turnLabel.setText(text);
    }
}
