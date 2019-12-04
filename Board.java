import java.util.*;

class Board {

    private int[][] boardMatrix;
    private int winLength;

    /**
     * Board constructor and initialises every space to '0' empty.
     *
     * @param boardSize board dimensions
     * @param winLength winning length to end game
     *
     */
     Board(int boardSize, int winLength) {
        this.boardMatrix = new int[boardSize][boardSize];
        this.winLength = winLength;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                this.getBoardMatrix()[row][col] = 0;
            }
        }
     }


    /**
     * Place a move on the board
     *
     * @param player player's representation
     * @param row  move location x axis
     * @param col  move location y axis
     */
    void makeMoveMatrix(final Board board, final int player, final int row, final int col) {
            if (player == 1) board.getBoardMatrix()[row][col] = 1;
            else board.getBoardMatrix()[row][col] = 2;
    }


    /**
     * Prints board matrix to cmd line for testing purposes
     */
    void printBoardMatrix() {
        for (int i = 0; i < getBoardSize(); i++) {
            for (int j = 0; j < getBoardSize(); j++) {
                System.out.print(getBoardMatrix()[i][j] + " ");
            }
            System.out.println();
        }
    }


    /**
     * Copy a board matrix
     *
     * @param board the board to copy
     */
    Board(final Board board) {
        int[][] matrixToCopy = board.getBoardMatrix();
        boardMatrix = new int[matrixToCopy.length][matrixToCopy.length];
        for (int i = 0; i < matrixToCopy.length; i++) {
            System.arraycopy(matrixToCopy[i], 0, boardMatrix[i], 0, matrixToCopy.length);
        }
    }


    /**
     * @return list of available moves as a list of int arrays
     */
     ArrayList<int[]> getAllAvailableMoves () {
        ArrayList<int[]> availableMoves = new ArrayList<>();

        for (int row = 0; row < getBoardSize(); row++) {
            for (int col = 0; col < getBoardSize(); col++) {
                if (getBoardMatrix()[row][col] == 0) {
                    final int[] coordinate = new int[]{ row, col };
                    availableMoves.add(coordinate);
                }
            }
        }
        return availableMoves;
    }


    /**
     * Returns specific moves surrounding occupied squares
     *
     * @return array list of integer arrays
     *
     */
     ArrayList <int[]> getSpecificMoves() {

        int boardSize = getBoardSize();
        int boardEnd = boardSize - 1;
        int empty = 0;

        ArrayList <int[]> moveList = new ArrayList <>();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                if (getBoardMatrix()[row][col] != empty) continue;

                if (row != empty) { // 0
                    if (col != empty) { // 0
                        if (getBoardMatrix()[row-1][col-1] != empty || getBoardMatrix()[row][col-1] != empty) {
                            moveList.add(new int[] {row, col});
                            continue;
                        }
                    }
                    if (col < boardEnd) {
                        if (getBoardMatrix()[row-1][col+1] != empty || getBoardMatrix()[row][col+1] != empty) {
                            moveList.add(new int[] {row, col});
                            continue;
                        }
                    }
                    if(getBoardMatrix()[row-1][col] != empty) {
                        moveList.add(new int[] {row, col});
                        continue;
                    }
                }

                if (row < boardEnd) {
                    if (col != empty) { // 0
                        if (getBoardMatrix()[row+1][col-1] != empty || getBoardMatrix()[row][col-1] != empty) {
                            moveList.add(new int[] {row, col});
                            continue;
                        }
                    }
                    if (col < boardEnd) {
                        if (getBoardMatrix()[row+1][col+1] != empty || getBoardMatrix()[row][col+1] != empty) {
                            moveList.add(new int[] {row, col});
                            continue;
                        }
                    }
                    if (getBoardMatrix()[row+1][col] != empty) moveList.add(new int[]{row, col});
                }
            }
        }
        return moveList;
     }


    /**
     * Checks if a move is available for play
     *
     * @param row  move location x axis
     * @param col  move location y axis
     * @return true if move available else false if not
     *
     */
    boolean isMoveAvailable(final int row, final int col) {
        int[] temp = new int[] {row, col};
        return getAllAvailableMoves().stream().anyMatch(arr -> Arrays.equals(arr, temp));
    }


    /**
     * Removes a move just played from list of available moves
     */
    void removeMove(final int row, final int col) {
        getAllAvailableMoves().removeIf(a -> Arrays.equals(a, new int[]{ row, col }));
    }


    /**
     * Checks if there is a win on horizontal axis
     *
     * @param board   board to check
     * @param player  player to check if won
     * @param row     last played row location
     * @param col     last played column location
     *
     * @return true if win else false if no win
     *
     */
    private boolean isHorizontalWin(final Board board, final int player,
                                    final int row, final int col) {
        int count = 1;

        for (int c = col+1; c < getBoardSize(); c++) {
            if (board.getBoardMatrix()[row][c] != (player)) break;
            count++;
        }

        for (int c = col-1; c >= 0; c--) {
            if (board.getBoardMatrix()[row][c] != (player)) break;
            count++;
        }
        return (count == getWinLength());
    }

    /**
     * Checks if there is a win on vertical axis
     *
     * @param board   board to check
     * @param player  player to check if won
     * @param row     last played row location
     * @param col     last played column location
     *
     * @return true if win else false if no win
     *
     */
    private boolean isVerticalWin(final Board board, final int player, final int row, final int col) {

        int count = 1;

        for (int r = row+1; r < getBoardSize(); r++) {
            if (board.getBoardMatrix()[r][col] != (player)) break;
            count++;
        }

        for (int r = row-1; r >= 0; r--) {
            if (board.getBoardMatrix()[r][col] != (player)) break;
            count++;
        }
        return (count == getWinLength());
    }


    /**
     * Checks if there is a win on left to right diagonals
     *
     * @param board   board to check
     * @param player  player to check if won
     * @param row     last played row location
     * @param col     last played column location
     *
     * @return true if win else false if no win
     *
     */
    private boolean isDiagonalWinRight(final Board board, final int player, final int row, final int col) {

        int count = 1;

        for (int r = row-1, c = col-1; r >= 0 && c >= 0; r--, c--) {
            if (board.getBoardMatrix()[r][c] != (player)) break;
            count++;
        }

        for (int r = row+1, c = col+1; r < getBoardSize() && c < getBoardSize(); r++, c++) {
            if (board.getBoardMatrix()[r][c] != (player)) break;
            count++;
        }
        return (count == getWinLength());
    }

    /**
     * Checks if there is a win on right to left diagonals
     *
     * @param board   board to check
     * @param player  player to check if won
     * @param row     last played row location
     * @param col     last played column location
     *
     * @return true if win else false if no win
     *
     */
    private boolean isDiagonalWinLeft(final Board board, final int player, final int row, final int col) {
        int count = 1;

        for (int r = row-1, c = col+1; r >= 0 && r < getBoardSize() && c >= 0 && c < getBoardSize(); r--, c++) {
            if (board.getBoardMatrix()[r][c] != (player)) break;
            count++;
        }

        for (int r = row+1, c = col-1; r >= 0 && r < getBoardSize() && c >= 0 && c < getBoardSize(); r++, c--) {
            if (board.getBoardMatrix()[r][c] != (player)) break;
            count++;
        }
        return (count == getWinLength());
    }


    /**
     * Checks all axis to find win from last played move
     *
     * @param game   current game being played
     * @param player  player to check if won
     * @param row     last played row location
     * @param col     last played column location
     *
     * @return true if win else false if no win
     *
     */

     boolean checkWinAll(final Game game, final int player, final int row, final int col) {
        if      ((isDiagonalWinLeft(game.getGameBoard(), player, row, col)) ||
                (isDiagonalWinRight(game.getGameBoard(), player, row, col)) ||
                (isVerticalWin(game.getGameBoard(), player, row, col))      ||
                 isHorizontalWin(game.getGameBoard(), player, row, col))
        {
            game.setWinner(player);
            return true;
        }
        else return false;
    }


    /**
     * @return int[][] boardMatrix
     */
    int[][] getBoardMatrix () { return boardMatrix; }


    /**
     * @return int boardSize
     */
    private int getBoardSize() { return boardMatrix.length; }


    /**
     * @return int winLength
     */
    private int getWinLength() { return winLength; }
}
