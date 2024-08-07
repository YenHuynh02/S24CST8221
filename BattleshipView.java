import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleshipView extends JPanel {
    private JButton[][] playerButtons;
    private JButton[][] aiButtons;
    private JLabel turnLabel;
    private JTextArea statusArea;
    private JTextField chatBox;
    private JButton swapButton;
    private JButton placeButton;
    private JButton resetButton;
    private JButton networkButton;
    private JButton startButton;
    private JButton helpButton;
    private JCheckBox developerModeCheckBox;
    private ImageIcon hitIcon;
    private ImageIcon missIcon;
    private ImageIcon shipHIcon;
    private ImageIcon shipVIcon;
    private ImageIcon subHIcon;
    private ImageIcon subVIcon;
    private ImageIcon carrierHIcon;
    private ImageIcon carrierVIcon;
    private JPanel mainPanel;
    private JTextArea playerShips;
    private JTextArea enemyShips;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private MainMenu menu;
    private boolean isVietnamese;

    public BattleshipView() {
        // Load images
        hitIcon = new ImageIcon(BattleshipView.class.getResource("/hit.png"));
        missIcon = new ImageIcon(BattleshipView.class.getResource("/miss.png"));
        shipHIcon = new ImageIcon(BattleshipView.class.getResource("/shipH.png"));
        shipVIcon = new ImageIcon(BattleshipView.class.getResource("/shipV.png"));
        subHIcon = new ImageIcon(BattleshipView.class.getResource("/suH.png"));
        subVIcon = new ImageIcon(BattleshipView.class.getResource("/suV.png"));
        carrierHIcon = new ImageIcon(BattleshipView.class.getResource("/caH.png"));
        carrierVIcon = new ImageIcon(BattleshipView.class.getResource("/caV.png"));

        // Create card layout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create introduction and main panels
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.8);
        int height = (int) (screenSize.getHeight() * 0.8);

        JPanel introductionPanel = createIntroductionPanel(width, height);
        mainPanel = createMainPanel(width, height);

        cardPanel.add(introductionPanel, "Introduction");
        cardPanel.add(mainPanel, "Game");

        // Initially show the introduction panel
        cardLayout.show(cardPanel, "Introduction");
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }

    private JPanel createMainPanel(int width, int height) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        menu = new MainMenu(this);
        setJMenuBar(menu);

        // Top panel for logo and buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 82, 10));
        topPanel.setPreferredSize(new Dimension(width, height / 10));
        topPanel.setBackground(Color.BLACK);
        panel.add(topPanel, BorderLayout.NORTH);

        networkButton = createButton("Network", new Dimension(81, 42));
        topPanel.add(networkButton);
        
        resetButton = createButton("Reset", new Dimension(81, 42));
        topPanel.add(resetButton);

        ImageIcon logoImage = new ImageIcon(BattleshipView.class.getResource("/Logo.png"));
        JLabel logo = new JLabel(logoImage);
        logo.setPreferredSize(new Dimension(60, 60));
        topPanel.add(logo);

        helpButton = createButton("Help", new Dimension(81, 42));
        helpButton.addActionListener(e -> cardLayout.show(cardPanel, "Introduction"));  // Show introduction panel when Help is clicked
        topPanel.add(helpButton);

        placeButton = createButton("Place", new Dimension(81, 42));
        topPanel.add(placeButton);

        developerModeCheckBox = new JCheckBox("Developer Mode");
        developerModeCheckBox.setBackground(Color.BLACK);
        developerModeCheckBox.setForeground(Color.WHITE);
        topPanel.add(developerModeCheckBox);

        // Center panel for game grid and player stats
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setPreferredSize(new Dimension(width, height * 8 / 10));
        centerPanel.setBackground(Color.BLACK);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Game grid panel with border
        JPanel gameGridPanel = new JPanel(new GridLayout(10, 20, 0, 0)); // Adjusted to 10x20 to include AI's board
        gameGridPanel.setPreferredSize(new Dimension(width * 3 / 4, height));
        gameGridPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));
        centerPanel.add(gameGridPanel, BorderLayout.WEST);

        playerButtons = new JButton[10][10];
        aiButtons = new JButton[10][10];
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                playerButtons[row][col] = new JButton();
                playerButtons[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gameGridPanel.add(playerButtons[row][col]);

                aiButtons[row][col] = new JButton();
                aiButtons[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                aiButtons[row][col].setEnabled(false); // AI buttons are not interactable
                gameGridPanel.add(aiButtons[row][col]);
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

        // My ships panel
        JPanel yourShipsPanel = new JPanel();
        yourShipsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        yourShipsPanel.setBackground(new java.awt.Color(255, 247, 231));
        playerShips = new JTextArea("Your Ships\nDestroyer: 3/3\nSubmarine: 4/4\nCarrier: 5/5");
        playerShips.setFont(new Font("Arial", Font.BOLD, 12));
        playerShips.setEditable(false);
        playerShips.setBackground(new java.awt.Color(255, 247, 231));
        yourShipsPanel.add(playerShips);
        statsPanel.add(yourShipsPanel);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Enemy ships panel
        JPanel enemyShipsPanel = new JPanel();
        enemyShipsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        enemyShipsPanel.setBackground(new java.awt.Color(255, 247, 231));
        enemyShips = new JTextArea("Enemy Ships\nDestroyer: 3/3\nSubmarine: 4/4\nCarrier: 5/5");
        enemyShips.setFont(new Font("Arial", Font.BOLD, 12));
        enemyShips.setEditable(false);
        enemyShips.setBackground(new java.awt.Color(255, 247, 231));
        enemyShipsPanel.add(enemyShips);
        statsPanel.add(enemyShipsPanel);

        // Combat log
        statusArea = new JTextArea("Initializing game boards\nSelect where to place your ships\nStarting combat");
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
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createIntroductionPanel(int width, int height) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JTextArea instructions = new JTextArea();
        instructions.setFont(new Font("Arial", Font.BOLD, 18));
        instructions.setEditable(false);
        instructions.setBackground(Color.BLACK);
        instructions.setForeground(Color.WHITE);
        instructions.setText("Welcome to Battleship\n\n" +
                "How to Play\n" +
                "Destroyer: 3 spaces\n" +
                "Submarine: 4 spaces\n" +
                "Carrier: 5 spaces\n" +
                "1. Place your ships on the grid\n" +
                "2. Click on a cell to attack the opponent's grid\n" +
                "3. First to sink all opponent's ships wins\n\n" +
                "Good luck!");

        panel.add(instructions, BorderLayout.CENTER);

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(100, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Game");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(startButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
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

    public JButton[][] getAiButtons() {
        return aiButtons;
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

    public JButton getNetworkButton() {
        return networkButton;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getHelpButton() {
        return helpButton;
    }

    public JCheckBox getDeveloperModeCheckBox() {
        return developerModeCheckBox;
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

    public ImageIcon getSubHIcon() {
        return subHIcon;
    }

    public ImageIcon getSubVIcon() {
        return subVIcon;
    }

    public ImageIcon getCarrierHIcon() {
        return carrierHIcon;
    }

    public ImageIcon getCarrierVIcon() {
        return carrierVIcon;
    }

    public void updateStatus(String status) {
        statusArea.append("\n" + status);
        statusArea.setCaretPosition(statusArea.getDocument().getLength());
    }

    public void setTurnLabel(String text) {
        turnLabel.setText(text);
    }

    public void updatePlayerShipsStatus(int destroyerRemaining, int submarineRemaining, int carrierRemaining) {
        if (isVietnamese) {
            playerShips.setText("Tau cua ban\nKhu truc: " + destroyerRemaining + "/3\nTau ngam: " + submarineRemaining + "/4\nTau san bay: " + carrierRemaining + "/5");
        } else {
            playerShips.setText("Your Ships\nDestroyer: " + destroyerRemaining + "/3\nSubmarine: " + submarineRemaining + "/4\nCarrier: " + carrierRemaining + "/5");
        }
    }

    public void updateEnemyShipsStatus(int destroyerRemaining, int submarineRemaining, int carrierRemaining) {
        if (isVietnamese) {
            enemyShips.setText("Tau dich\nKhu truc: " + destroyerRemaining + "/3\nTau ngam: " + submarineRemaining + "/4\nTau san bay: " + carrierRemaining + "/5");
        } else {
            enemyShips.setText("Enemy Ships\nDestroyer: " + destroyerRemaining + "/3\nSubmarine: " + submarineRemaining + "/4\nCarrier: " + carrierRemaining + "/5");
        }
    }

    public String showOrientationDialog() {
        Object[] options = isVietnamese ? new Object[]{"Ngang", "Doc"} : new Object[]{"Horizontal", "Vertical"};
        int n = JOptionPane.showOptionDialog(this,
                isVietnamese ? "Chon huong tau" : "Choose the ship orientation",
                isVietnamese ? "Huong tau" : "Ship Orientation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return (n == JOptionPane.YES_OPTION) ? "H" : "V";
    }

    public String showShipTypeDialog() {
        Object[] options = isVietnamese ? new Object[]{"Khu truc", "Tau ngam", "Tau san bay"} : new Object[]{"Destroyer", "Submarine", "Carrier"};
        int n = JOptionPane.showOptionDialog(this,
                isVietnamese ? "Chon loai tau" : "Choose the ship type",
                isVietnamese ? "Loai tau" : "Ship Type",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return (n == JOptionPane.YES_OPTION) ? "D" : (n == JOptionPane.NO_OPTION ? "S" : "C");
    }

    public void showGameEndDialog(String message) {
        JOptionPane.showMessageDialog(this, message, isVietnamese ? "Ket thuc tro choi" : "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setJMenuBar(MainMenu menu) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        // Remove this line: JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        menuBar.add(menu);
    }

    public void updateLanguageToVietnamese() {
        isVietnamese = true;
        turnLabel.setText("Luot cua ban");
        swapButton.setText("Doi");
        placeButton.setText("Dat");
        resetButton.setText("Dat lai");
        networkButton.setText("Mang");
        startButton.setText("Bat dau");
        updatePlayerShipsStatus(3, 4, 5);
        updateEnemyShipsStatus(3, 4, 5);
        statusArea.setText("Dang khoi dong bang tro choi\nChon noi dat tau cua ban\nBat dau chien dau");
    }

    public void updateLanguageToEnglish() {
        isVietnamese = false;
        turnLabel.setText("Player's turn");
        swapButton.setText("Swap");
        placeButton.setText("Place");
        resetButton.setText("Reset");
        networkButton.setText("Network");
        startButton.setText("Start");
        updatePlayerShipsStatus(3, 4, 5);
        updateEnemyShipsStatus(3, 4, 5);
        statusArea.setText("Initializing game boards\nSelect where to place your ships\nStarting combat");
    }

    public void updateAttackStatus(boolean isPlayer, int x, int y, boolean isHit) {
        String message = isVietnamese ? 
            (isPlayer ? "Nguoi choi tan cong o vi tri: " : "AI tan cong o vi tri: ") + x + ", " + y + (isHit ? " (trung)" : " (truot)") :
            (isPlayer ? "Player attacked at: " : "Computer attacked at: ") + x + ", " + y + (isHit ? " (hit)" : " (miss)");
        updateStatus(message);
    }
}
