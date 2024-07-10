import java.util.Random;

public class BattleshipModel {
    private char[][] playerBoard;
    private char[][] computerBoard;
    private boolean playerTurn;
    private boolean gameOver;
    private int playerHits;
    private int computerHits;
    private int lastComputerAttackX;
    private int lastComputerAttackY;
    private boolean lastComputerAttackResult;

    public BattleshipModel() {
        playerBoard = new char[10][10];
        computerBoard = new char[10][10];
        playerTurn = true;
        gameOver = false;
        playerHits = 0;
        computerHits = 0;

        // Initialize boards
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoard[i][j] = ' ';
                computerBoard[i][j] = ' ';
            }
        }

        // Place ships on computer board randomly
        placeComputerShips();
    }

    public void placePlayerShip(int x, int y, char orientation, int length) {
        if (orientation == 'H') {
            for (int i = 0; i < length; i++) {
                playerBoard[x][y + i] = 'S';
            }
        } else {
            for (int i = 0; i < length; i++) {
                playerBoard[x + i][y] = 'S';
            }
        }
    }

    private void placeComputerShips() {
        Random random = new Random();
        int[] shipLengths = {5, 4, 3, 3, 2};

        for (int length : shipLengths) {
            boolean placed = false;

            while (!placed) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                char orientation = random.nextBoolean() ? 'H' : 'V';

                if (canPlaceShip(computerBoard, x, y, orientation, length)) {
                    placeShip(computerBoard, x, y, orientation, length);
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceShip(char[][] board, int x, int y, char orientation, int length) {
        if (orientation == 'H') {
            if (y + length > 10) return false;
            for (int i = 0; i < length; i++) {
                if (board[x][y + i] == 'S') return false;
            }
        } else {
            if (x + length > 10) return false;
            for (int i = 0; i < length; i++) {
                if (board[x + i][y] == 'S') return false;
            }
        }
        return true;
    }

    private void placeShip(char[][] board, int x, int y, char orientation, int length) {
        if (orientation == 'H') {
            for (int i = 0; i < length; i++) {
                board[x][y + i] = 'S';
            }
        } else {
            for (int i = 0; i < length; i++) {
                board[x + i][y] = 'S';
            }
        }
    }

    public void playerAttack(int x, int y) {
        if (computerBoard[x][y] == 'S') {
            computerBoard[x][y] = 'H';
            playerHits++;
        } else if (computerBoard[x][y] == ' ') {
            computerBoard[x][y] = 'M';
        }
        playerTurn = false;
        checkGameOver();
    }

    public void computerAttack() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(10);
            y = random.nextInt(10);
        } while (playerBoard[x][y] == 'M' || playerBoard[x][y] == 'H');

        if (playerBoard[x][y] == 'S') {
            playerBoard[x][y] = 'H';
            computerHits++;
            lastComputerAttackResult = true;
        } else {
            playerBoard[x][y] = 'M';
            lastComputerAttackResult = false;
        }

        lastComputerAttackX = x;
        lastComputerAttackY = y;

        playerTurn = true;
        checkGameOver();
    }

    private void checkGameOver() {
        if (playerHits == 5) {
            gameOver = true;
        } else if (computerHits == 5) {
            gameOver = true;
        }
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        if (playerHits == 5) {
            return "Player";
        } else if (computerHits == 5) {
            return "Computer";
        }
        return "None";
    }

    public char[][] getPlayerBoard() {
        return playerBoard;
    }

    public char[][] getComputerBoard() {
        return computerBoard;
    }

    public int getLastComputerAttackX() {
        return lastComputerAttackX;
    }

    public int getLastComputerAttackY() {
        return lastComputerAttackY;
    }

    public boolean getLastComputerAttackResult() {
        return lastComputerAttackResult;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }
}
