import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleshipController {
    private BattleshipView view;
    private BattleshipModel model;
    private boolean isPlacingShips;
    private boolean playerShipPlaced;

    public BattleshipController(BattleshipView view, BattleshipModel model) {
        this.view = view;
        this.model = model;
        this.isPlacingShips = true;
        this.playerShipPlaced = false;

        initializeGame();
        addEventListeners();
    }

    private void initializeGame() {
        view.updateStatus("Welcome to Battleship! Place your ship.");
        view.setTurnLabel("Player's turn");
        view.updatePlayerShipsStatus(5); // Initialize player ships status
        view.updateEnemyShipsStatus(5); // Initialize enemy ships status
        view.getSwapButton().setEnabled(false); // Disable swap button at start
    }

    private void addEventListeners() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int finalRow = row;
                int finalCol = col;
                view.getPlayerButtons()[row][col].addActionListener(e -> {
                    if (isPlacingShips) {
                        placePlayerShip(finalRow, finalCol);
                    } else if (model.isPlayerTurn()) {
                        playerAttack(finalRow, finalCol);
                        view.getSwapButton().setEnabled(true); // Enable swap button after player's turn
                    }
                });
            }
        }

        view.getSwapButton().addActionListener(e -> {
            if (!model.isPlayerTurn() && !model.isGameOver()) {
                aiTurn();
                view.getSwapButton().setEnabled(false); // Disable swap button after AI's turn
            }
        });

        view.getPlaceButton().addActionListener(e -> {
            if (isPlacingShips) {
                view.updateStatus("Click on the board to place your ship.");
            } else {
                view.updateStatus("You have already placed your ship.");
            }
        });

        view.getResetButton().addActionListener(e -> resetGame());

        view.getChatBox().addActionListener(e -> {
            String message = view.getChatBox().getText();
            if (!message.isEmpty()) {
                view.updateStatus(message);
                view.getChatBox().setText("");
            }
        });
    }

    private void placePlayerShip(int row, int col) {
        if (playerShipPlaced) {
            view.updateStatus("Ship already placed. Start attacking!");
            return;
        }

        String orientation = view.showOrientationDialog();
        if (model.canPlaceShip(model.getPlayerBoard(), row, col, orientation.charAt(0))) {
            model.placePlayerShip(row, col, orientation.charAt(0));
            for (int i = 0; i < model.SHIP_SIZE; i++) {
                if (orientation.equals("H")) {
                    view.getPlayerButtons()[row][col + i].setIcon(view.getShipHIcon());
                } else {
                    view.getPlayerButtons()[row + i][col].setIcon(view.getShipVIcon());
                }
            }
            playerShipPlaced = true;
            isPlacingShips = false;
            view.updateStatus("Ship placed. Start attacking!");
            view.updatePlayerShipsStatus(0); // Update player ships status to 0/5
            view.updateEnemyShipsStatus(0); // Update enemy ships status to 0/5
            view.getSwapButton().setEnabled(true); // Enable swap button after placing the ship
        } else {
            view.updateStatus("Invalid ship placement. Try again.");
        }
    }

    private void playerAttack(int row, int col) {
        if (model.isGameOver()) {
            return;
        }

        boolean hit = model.playerAttack(row, col);
        if (hit) {
            view.getPlayerButtons()[row][col].setIcon(view.getHitIcon());
            view.updateStatus("Player attacked at: " + row + ", " + col + " (hit)");
            view.updateEnemyShipsStatus(model.SHIP_SIZE - model.getPlayerHits()); // Update enemy ships status
        } else {
            view.getPlayerButtons()[row][col].setIcon(view.getMissIcon());
            view.updateStatus("Player attacked at: " + row + ", " + col + " (miss)");
        }

        if (model.isGameOver()) {
            endGame();
        } else {
            view.setTurnLabel("AI's turn");
        }
    }

    private void aiTurn() {
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] attackCoordinates = model.computerAttack();
                int row = attackCoordinates[0];
                int col = attackCoordinates[1];

                if (model.getPlayerBoard()[row][col] == 'H') {
                    view.getPlayerButtons()[row][col].setIcon(view.getHitIcon());
                    view.updateStatus("Computer attacked at: " + row + ", " + col + " (hit)");
                    view.updatePlayerShipsStatus(model.SHIP_SIZE - model.getComputerHits()); // Update player ships status
                } else {
                    view.getPlayerButtons()[row][col].setIcon(view.getMissIcon());
                    view.updateStatus("Computer attacked at: " + row + ", " + col + " (miss)");
                }

                if (model.isGameOver()) {
                    endGame();
                } else {
                    view.setTurnLabel("Player's turn");
                }

                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    private void endGame() {
        view.showGameEndDialog(model.getWinner() + " wins!");
        if (model.getWinner().equals("Computer")) {
            revealComputerShips();
        }
    }

    private void revealComputerShips() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (model.getComputerBoard()[row][col] == 'S') {
                    view.getPlayerButtons()[row][col].setIcon(view.getShipHIcon());
                }
            }
        }
    }

    private void resetGame() {
        model.resetGame();
        view.updateStatus("Game reset. Place your ship.");
        view.setTurnLabel("Player's turn");
        isPlacingShips = true;
        playerShipPlaced = false;

        resetGrid(view.getPlayerButtons());
        view.getSwapButton().setEnabled(false); // Disable swap button at reset
        view.updatePlayerShipsStatus(5); // Reset player ships status
        view.updateEnemyShipsStatus(5); // Reset enemy ships status
    }

    private void resetGrid(JButton[][] buttons) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                buttons[row][col].setIcon(null);
            }
        }
    }
}
