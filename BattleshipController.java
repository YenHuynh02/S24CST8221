import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.SwingWorker;

public class BattleshipController {
    private BattleshipModel model;
    private BattleshipView view;
    private boolean placingShip;
    private int shipLength;
    private char orientation;

    public BattleshipController(BattleshipModel model, BattleshipView view) {
        this.model = model;
        this.view = view;
        this.placingShip = false;
        this.shipLength = 5; // Example ship length1
        this.orientation = 'H'; // Default orientation
        view.setButtonListener(new ButtonListener());
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();

            if (button == view.getSwapButton()) {
                if (!model.isGameOver()) {
                    view.updateTurnLabel("AI turn");
                    new SwapTask().execute();
                }
                return;
            }

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (button == view.getGameGridButton(i, j)) {
                        if (placingShip) {
                            placeShip(i, j);
                        } else if (!model.isGameOver() && model.isPlayerTurn()) {
                            model.playerAttack(i, j);
                            String attackStatus = "Player attacked at: " + i + ", " + j;
                            view.updateStatus(attackStatus);
                            System.out.println(attackStatus); // Debug statement
                            updateView();
                        }
                    }
                }
            }
        }
    }

    private void placeShip(int x, int y) {
        // Example: Place a ship at the specified location
        if (orientation == 'H') {
            if (y + shipLength <= 10) {
                model.placePlayerShip(x, y, 'H', shipLength);
                placingShip = false;
            } else {
                // Handle invalid placement
                view.updateStatus("Invalid ship placement.");
            }
        } else {
            if (x + shipLength <= 10) {
                model.placePlayerShip(x, y, 'V', shipLength);
                placingShip = false;
            } else {
                // Handle invalid placement
                view.updateStatus("Invalid ship placement.");
            }
        }
        updateView();
    }

    private void updateView() {
        char[][] playerBoard = model.getPlayerBoard();
        char[][] computerBoard = model.getComputerBoard();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                char status = computerBoard[i][j];
                view.setButtonIcon(i, j, status);
            }
        }

        if (model.isGameOver()) {
            String winner = model.getWinner();
            view.updateStatus(winner + " wins!");
        }

        if (model.isPlayerTurn()) {
            view.updateTurnLabel("Player's turn");
        } else {
            view.updateTurnLabel("AI turn");
        }
    }

    private class SwapTask extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
            Thread.sleep(3000); // 3-second delay
            return null;
        }

        @Override
        protected void done() {
            if (!model.isGameOver()) {
                model.computerAttack();
                int x = model.getLastComputerAttackX();
                int y = model.getLastComputerAttackY();
                boolean hit = model.getLastComputerAttackResult();
                String attackStatus = "Computer attacked at: " + x + ", " + y + (hit ? " (hit)" : " (miss)");
                view.updateStatus(attackStatus);
                System.out.println(attackStatus); // Debug statement
                updateView();
                model.setPlayerTurn(true); // Switch back to player's turn
                updateView(); // Update the view to reflect the player's turn
            }
        }
    }
}
