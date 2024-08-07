import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleshipController {
    private BattleshipView view;
    private BattleshipModel model;
    private boolean isPlacingShips;
    private boolean playerShipPlaced;
    private Network network;

    public BattleshipController(BattleshipView view, BattleshipModel model, Network network) {
        this.view = view;
        this.model = model;
        this.network = network;
        this.isPlacingShips = true;
        this.playerShipPlaced = false;

        initializeGame();
        addEventListeners();
    }

    private void initializeGame() {
        view.updateStatus("Welcome to Battleship! Place your ship.");
        view.setTurnLabel("Player's turn");
        view.updatePlayerShipsStatus(3, 4, 5); // Initialize player ships status
        view.updateEnemyShipsStatus(3, 4, 5); // Initialize enemy ships status
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
                network.sendMessage(message);
            }
        });

        view.getDeveloperModeCheckBox().addActionListener(e -> {
            if (view.getDeveloperModeCheckBox().isSelected()) {
                showAiBoard();
            } else {
                hideAiBoard();
            }
        });
    }

    private void placePlayerShip(int row, int col) {
        if (playerShipPlaced) {
            view.updateStatus("Ship already placed. Start attacking!");
            return;
        }

        String orientation = view.showOrientationDialog();
        String shipType = view.showShipTypeDialog();
        model.setCurrentShipType(shipType.charAt(0));

        if (model.canPlaceShip(model.getPlayerBoard(), row, col, orientation.charAt(0), model.getShipSize(shipType.charAt(0)))) {
            model.placePlayerShip(row, col, orientation.charAt(0), shipType.charAt(0));
            ImageIcon shipIcon = getShipIcon(shipType.charAt(0), orientation.charAt(0));
            for (int i = 0; i < model.getShipSize(shipType.charAt(0)); i++) {
                if (orientation.equals("H")) {
                    view.getPlayerButtons()[row][col + i].setIcon(shipIcon);
                } else {
                    view.getPlayerButtons()[row + i][col].setIcon(shipIcon);
                }
            }
            playerShipPlaced = true;
            isPlacingShips = false;
            view.updateStatus("Ship placed. Start attacking!");
            updatePlayerShipsStatus(shipType.charAt(0));
            view.updateEnemyShipsStatus(3, 4, 5); // Initialize enemy ships status
            view.getSwapButton().setEnabled(true); // Enable swap button after placing the ship
            network.sendMessage("READY");
        } else {
            view.updateStatus("Invalid ship placement. Try again.");
        }
    }

    private void updatePlayerShipsStatus(char shipType) {
        switch (shipType) {
            case 'D':
                view.updatePlayerShipsStatus(0, model.getRemainingShips('S'), model.getRemainingShips('C'));
                break;
            case 'S':
                view.updatePlayerShipsStatus(model.getRemainingShips('D'), 0, model.getRemainingShips('C'));
                break;
            case 'C':
                view.updatePlayerShipsStatus(model.getRemainingShips('D'), model.getRemainingShips('S'), 0);
                break;
        }
    }

    private void updateEnemyShipsStatus(char shipType) {
        switch (shipType) {
            case 'D':
                view.updateEnemyShipsStatus(0, model.getRemainingShips('S'), model.getRemainingShips('C'));
                break;
            case 'S':
                view.updateEnemyShipsStatus(model.getRemainingShips('D'), 0, model.getRemainingShips('C'));
                break;
            case 'C':
                view.updateEnemyShipsStatus(model.getRemainingShips('D'), model.getRemainingShips('S'), 0);
                break;
        }
    }

    private ImageIcon getShipIcon(char shipType, char orientation) {
        switch (shipType) {
            case 'D': return (orientation == 'H') ? view.getShipHIcon() : view.getShipVIcon();
            case 'S': return (orientation == 'H') ? view.getSubHIcon() : view.getSubVIcon();
            case 'C': return (orientation == 'H') ? view.getCarrierHIcon() : view.getCarrierVIcon();
            default: return null;
        }
    }

    private void playerAttack(int row, int col) {
        if (model.isGameOver()) {
            return;
        }

        boolean hit = model.playerAttack(row, col);
        if (hit) {
            view.getPlayerButtons()[row][col].setIcon(view.getHitIcon());
            view.updateAttackStatus(true, row, col, true);
            view.updateEnemyShipsStatus(model.getRemainingShips('D'), model.getRemainingShips('S'), model.getRemainingShips('C')); // Update enemy ships status
        } else {
            view.getPlayerButtons()[row][col].setIcon(view.getMissIcon());
            view.updateAttackStatus(true, row, col, false);
        }

        network.sendMessage("MOVE " + row + " " + col + " " + (hit ? "HIT" : "MISS"));

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
                    view.updateAttackStatus(false, row, col, true);
                    view.updatePlayerShipsStatus(model.getRemainingShips('D'), model.getRemainingShips('S'), model.getRemainingShips('C')); // Update player ships status
                } else {
                    view.getPlayerButtons()[row][col].setIcon(view.getMissIcon());
                    view.updateAttackStatus(false, row, col, false);
                }

                network.sendMessage("AI MOVE " + row + " " + col);

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
        network.sendMessage("GAME OVER " + model.getWinner());
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

    private void showAiBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                char cell = model.getComputerBoard()[row][col];
                if (cell == 'S') {
                    view.getAiButtons()[row][col].setIcon(view.getShipHIcon());
                } else if (cell == 'H') {
                    view.getAiButtons()[row][col].setIcon(view.getHitIcon());
                } else if (cell == 'M') {
                    view.getAiButtons()[row][col].setIcon(view.getMissIcon());
                } else {
                    view.getAiButtons()[row][col].setIcon(null);
                }
            }
        }
    }

    private void hideAiBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                view.getAiButtons()[row][col].setIcon(null);
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
        resetGrid(view.getAiButtons());
        view.getSwapButton().setEnabled(false); // Disable swap button at reset
        view.updatePlayerShipsStatus(3, 4, 5); // Reset player ships status
        view.updateEnemyShipsStatus(3, 4, 5); // Reset enemy ships status
        network.sendMessage("RESET");
    }

    private void resetGrid(JButton[][] buttons) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                buttons[row][col].setIcon(null);
            }
        }
    }
}
