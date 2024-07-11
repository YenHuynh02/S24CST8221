import java.util.Random;

public class BattleshipModel {
    private char[][] playerBoard;
    private char[][] computerBoard;
    private boolean playerTurn;
    private boolean gameOver;
    private int playerHits;
    private int computerHits;
    public final int SHIP_SIZE = 5; // Size of the ship

    public BattleshipModel() {
        playerBoard = new char[10][10];
        computerBoard = new char[10][10];
        playerTurn = true;
        gameOver = false;
        playerHits = 0;
        computerHits = 0;

        // Initialize boards with empty characters
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoard[i][j] = ' ';
                computerBoard[i][j] = ' ';
            }
        }

        // Randomly place computer ship
        placeComputerShip();
    }

    public char[][] getPlayerBoard() {
        return playerBoard;
    }

    public char[][] getComputerBoard() {
        return computerBoard;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean playerAttack(int x, int y) {
        boolean hit = false;
        if (computerBoard[x][y] == ' ') {
            computerBoard[x][y] = 'M';
        } else if (computerBoard[x][y] == 'S') {
            computerBoard[x][y] = 'H';
            playerHits++;
            hit = true;
        }
        playerTurn = false;
        checkGameOver();
        return hit;
    }

    public int[] computerAttack() {
        int x, y;
        Random rand = new Random();
        do {
            x = rand.nextInt(10);
            y = rand.nextInt(10);
        } while (playerBoard[x][y] == 'M' || playerBoard[x][y] == 'H');

        if (playerBoard[x][y] == ' ') {
            playerBoard[x][y] = 'M';
        } else if (playerBoard[x][y] == 'S') {
            playerBoard[x][y] = 'H';
            computerHits++;
        }
        playerTurn = true;
        checkGameOver();
        return new int[]{x, y};
    }

    public void placePlayerShip(int x, int y, char orientation) {
        if (orientation == 'H') {
            for (int i = 0; i < SHIP_SIZE; i++) {
                playerBoard[x][y + i] = 'S';
            }
        } else {
            for (int i = 0; i < SHIP_SIZE; i++) {
                playerBoard[x + i][y] = 'S';
            }
        }
    }

    private void placeComputerShip() {
        Random rand = new Random();
        boolean placed = false;
        while (!placed) {
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);
            char orientation = rand.nextBoolean() ? 'H' : 'V';
            if (canPlaceShip(computerBoard, x, y, orientation)) {
                placeShip(computerBoard, x, y, orientation);
                placed = true;
            }
        }
    }

    public boolean canPlaceShip(char[][] board, int x, int y, char orientation) {
        if (orientation == 'H') {
            if (y + SHIP_SIZE > 10) return false;
            for (int i = 0; i < SHIP_SIZE; i++) {
                if (board[x][y + i] != ' ') return false;
            }
        } else {
            if (x + SHIP_SIZE > 10) return false;
            for (int i = 0; i < SHIP_SIZE; i++) {
                if (board[x + i][y] != ' ') return false;
            }
        }
        return true;
    }

    private void placeShip(char[][] board, int x, int y, char orientation) {
        if (orientation == 'H') {
            for (int i = 0; i < SHIP_SIZE; i++) {
                board[x][y + i] = 'S';
            }
        } else {
            for (int i = 0; i < SHIP_SIZE; i++) {
                board[x + i][y] = 'S';
            }
        }
    }

    private void checkGameOver() {
        if (playerHits >= SHIP_SIZE) {
            gameOver = true;
        } else if (computerHits >= SHIP_SIZE) {
            gameOver = true;
        }
    }

    public String getWinner() {
        if (playerHits >= SHIP_SIZE) {
            return "Player";
        } else if (computerHits >= SHIP_SIZE) {
            return "Computer";
        }
        return null;
    }

    public int getPlayerHits() {
        return playerHits;
    }

    public int getComputerHits() {
        return computerHits;
    }

    public void resetGame() {
        playerBoard = new char[10][10];
        computerBoard = new char[10][10];
        playerTurn = true;
        gameOver = false;
        playerHits = 0;
        computerHits = 0;

        // Initialize boards with empty characters
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoard[i][j] = ' ';
                computerBoard[i][j] = ' ';
            }
        }

        // Randomly place computer ship
        placeComputerShip();
    }
}
