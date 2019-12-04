import java.util.*;

public class Game {

    private boolean isGameOver = false;
    final private int opponentType;
    private int winner = 0;
    private int drawCount = 0;
    final private int depth;

    final Coordinates coordinates;
    final private Board gameBoard;
    final private Minimax ai;
    BoardGUI GUI;


    /**
     * Game constructor creates all components needed for playing a game.
     *
     * @param boardSize game board size.
     * @param winLength winning length needed to end game.
     * @param opponentType opponent type.
     * @param depth depth AI minimax will search to.
     *
     */
    Game(final int boardSize, final int winLength, final int opponentType, final int depth) {
        this.gameBoard = new Board(boardSize, winLength);
        this.ai = new Minimax(gameBoard);
        this.GUI = new BoardGUI();
        this.opponentType = opponentType;
        this.coordinates = new Coordinates(0, 0);
        this.depth = depth;
    }


    /**
     * Used in Human vs. AI Random - Selects a random move from a list of available moves.
     *
     * @param game the game that includes the board to make a move on.
     *
     */
    void makeMoveAIRandom(Game game) {

        Random random = new Random();

        int index = random.nextInt(game.getGameBoard().getAllAvailableMoves().size());

        int[] move = game.getGameBoard().getAllAvailableMoves().get(index);
        game.getGameBoard().getAllAvailableMoves().remove(index);

        int row = move[0];
        int col = move[1];

        GUI.getTileBoard()[row][col].makeMoveGUI(game.getGameBoard(), 1, row, col);

        setGameOver(game.getGameBoard().checkWinAll(game, 1, row, col));
    }


    /**
     * Chooses and makes a move for the AI
     *
     * @param game the game that includes the board to make a move on.
     * @param depth depth of the tree to search for a move.
     * @param player the player to make a move for. 2 - black, 1 - white.
     * @param forBlack true - black's turn, false - white's (AI) turn.
     *
     */
    void makeMoveMinimax(final Game game, final int depth, final int player, boolean forBlack) {

        int[] move = ai.getBestMove(depth, player, forBlack);

        int row = move[0];
        int col = move[1];

        // makes move on matrix and tile
        GUI.getTileBoard()[row][col].makeMoveGUI(gameBoard, player, row, col);

        // checks if game over
        setGameOver(gameBoard.checkWinAll(game, player, row, col));

        // removes played move from available moves list
        gameBoard.removeMove(row, col);
    }


    /**
     * FOR TESTING - NOT USED IN FINAL PROGRAM.
     *
     * Chooses and makes a move on the matrix for the AI.
     *
     * @param game the game that includes the board to make a move on.
     * @param depth depth of the tree to search for a move.
     * @param player the player to make a move for. 2 - black, 1 - white.
     * @param forBlack true - black's turn, false - white's (AI) turn.
     *
     */
    void makeMoveMinimaxMatrix(final Game game, final int depth, final int player, boolean forBlack) {

        int[] move = ai.getBestMove(depth, player, forBlack);

        int row = move[0];
        int col = move[1];

        System.out.println(row + " " + col);

        game.getGameBoard().makeMoveMatrix(game.getGameBoard(), player, row, col);

        if (getGameBoard().checkWinAll(game, player, row, col)) {
            if (player == 1) setWinner(1);
            else setWinner(2);
            System.out.println("Player " + player + " wins!");
            setGameOver(true);
        }
        getGameBoard().removeMove(row, col);
    }


    /**
     * Chooses and makes a move for the player
     *
     * @param game the game that includes the board to make a move on.
     * @param player player to make the move for.
     *
     */
    void playerMakeMove(final Game game, final int player) {

        int row = game.coordinates.getRow();
        int col = game.coordinates.getCol();

        if (game.getGameBoard().isMoveAvailable(row, col)){
            GUI.getTileBoard()[row][col].makeMoveGUI(game.getGameBoard(), player, row, col);

            setGameOver(game.gameBoard.checkWinAll(game, player, row, col));

            game.getGameBoard().removeMove(row, col);
        }
    }

    Board getGameBoard() { return this.gameBoard; }

    boolean getGameOver () { return this.isGameOver; }

    private void setGameOver (boolean result) { this.isGameOver = result; }

    int getOpponentType() { return opponentType; }

    int getWinner() { return winner; }

    void setWinner(int winner) { this.winner = winner; }

    int getDrawCount() { return drawCount; }

    void drawCountPlus1() { this.drawCount++; }

    int getDepth() { return depth; }


    /**
     * Class Coordinates stores the current move played as row, column coordinates.
     */
    class Coordinates {
        int row;
        int col;

        Coordinates(int row, int col) {
            this.row = row;
            this.col = col;
        }

        int getRow() { return row; }

        int getCol() {
            return col;
        }

        void setRow(int row) {
            this.row = row;
        }

        void setCol(int col) {
            this.col = col;
        }
    }
}


