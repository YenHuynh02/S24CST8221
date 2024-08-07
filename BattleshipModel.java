import java.util.Random;

public class BattleshipModel {
    private char[][] playerBoard;
    private char[][] computerBoard;
    private boolean playerTurn;
    private boolean gameOver;
    private int playerHits;
    private int computerHits;
    private int destroyerRemaining;
    private int submarineRemaining;
    private int carrierRemaining;
    public final int DESTROYER_SIZE = 3;
    public final int SUBMARINE_SIZE = 4;
    public final int CARRIER_SIZE = 5;
    private char currentShipType;
    
    public BattleshipModel() {
        playerBoard = new char[10][10];
        computerBoard = new char[10][10];
        playerTurn = true;
        gameOver = false;
        playerHits = 0;
        computerHits = 0;
        destroyerRemaining = DESTROYER_SIZE;
        submarineRemaining = SUBMARINE_SIZE;
        carrierRemaining = CARRIER_SIZE;

        // Initialize boards with empty characters
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoard[i][j] = ' ';
                computerBoard[i][j] = ' ';
            }
        }

        // Randomly place computer ships
        placeComputerShip('D', DESTROYER_SIZE);
        placeComputerShip('S', SUBMARINE_SIZE);
        placeComputerShip('C', CARRIER_SIZE);
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

    public char getCurrentShipType() {
        return currentShipType;
    }

    public int getShipSize(char shipType) {
        switch (shipType) {
            case 'D': return DESTROYER_SIZE;
            case 'S': return SUBMARINE_SIZE;
            case 'C': return CARRIER_SIZE;
        }
        return 0;
    }

    public int getRemainingShips(char shipType) {
        switch (shipType) {
            case 'D': return destroyerRemaining;
            case 'S': return submarineRemaining;
            case 'C': return carrierRemaining;
        }
        return 0;
    }

    public void setCurrentShipType(char shipType) {
        this.currentShipType = shipType;
        getShipSize(shipType);
    }

    public boolean playerAttack(int x, int y) {
        boolean hit = false;
        if (computerBoard[x][y] == ' ') {
            computerBoard[x][y] = 'M';
        } else if (computerBoard[x][y] == 'S') {
            computerBoard[x][y] = 'H';
            playerHits++;
            updateComputerShipsRemaining(currentShipType);
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
            updatePlayerShipsRemaining(currentShipType);
        }
        playerTurn = true;
        checkGameOver();
        return new int[]{x, y};
    }

    public void placePlayerShip(int x, int y, char orientation, char shipType) {
        if (orientation == 'H') {
            for (int i = 0; i < getShipSize(shipType); i++) {
                playerBoard[x][y + i] = 'S';
            }
        } else {
            for (int i = 0; i < getShipSize(shipType); i++) {
                playerBoard[x + i][y] = 'S';
            }
        }
    }

    private void placeComputerShip(char shipType, int shipSize) {
        Random rand = new Random();
        boolean placed = false;
        while (!placed) {
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);
            char orientation = rand.nextBoolean() ? 'H' : 'V';
            if (canPlaceShip(computerBoard, x, y, orientation, shipSize)) {
                placeShip(computerBoard, x, y, orientation, shipSize);
                placed = true;
            }
        }
    }

    public boolean canPlaceShip(char[][] board, int x, int y, char orientation, int shipSize) {
        if (orientation == 'H') {
            if (y + shipSize > 10) return false;
            for (int i = 0; i < shipSize; i++) {
                if (board[x][y + i] != ' ') return false;
            }
        } else {
            if (x + shipSize > 10) return false;
            for (int i = 0; i < shipSize; i++) {
                if (board[x + i][y] != ' ') return false;
            }
        }
        return true;
    }

    private void placeShip(char[][] board, int x, int y, char orientation, int shipSize) {
        if (orientation == 'H') {
            for (int i = 0; i < shipSize; i++) {
                board[x][y + i] = 'S';
            }
        } else {
            for (int i = 0; i < shipSize; i++) {
                board[x + i][y] = 'S';
            }
        }
    }

    private void checkGameOver() {
        if (playerHits >= DESTROYER_SIZE + SUBMARINE_SIZE + CARRIER_SIZE) {
            gameOver = true;
        } else if (computerHits >= DESTROYER_SIZE + SUBMARINE_SIZE + CARRIER_SIZE) {
            gameOver = true;
        }
    }

    public String getWinner() {
        if (playerHits >= DESTROYER_SIZE + SUBMARINE_SIZE + CARRIER_SIZE) {
            return "Player";
        } else if (computerHits >= DESTROYER_SIZE + SUBMARINE_SIZE + CARRIER_SIZE) {
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
        destroyerRemaining = DESTROYER_SIZE;
        submarineRemaining = SUBMARINE_SIZE;
        carrierRemaining = CARRIER_SIZE;

        // Initialize boards with empty characters
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoard[i][j] = ' ';
                computerBoard[i][j] = ' ';
            }
        }

        // Randomly place computer ships
        placeComputerShip('D', DESTROYER_SIZE);
        placeComputerShip('S', SUBMARINE_SIZE);
        placeComputerShip('C', CARRIER_SIZE);
    }

    public void updatePlayerShipsRemaining(char shipType) {
        switch (shipType) {
            case 'D': 
                destroyerRemaining--;
                break;
            case 'S': 
                submarineRemaining--;
                break;
            case 'C': 
                carrierRemaining--;
                break;
        }
    }

    public void updateComputerShipsRemaining(char shipType) {
        switch (shipType) {
            case 'D': 
                destroyerRemaining--;
                break;
            case 'S': 
                submarineRemaining--;
                break;
            case 'C': 
                carrierRemaining--;
                break;
        }
    }
}
