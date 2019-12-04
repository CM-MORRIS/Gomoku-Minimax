import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BoardGUI extends Application {

    private final int BOARD_SIZE = 15;
    private Tile[][] tileBoard = new Tile[BOARD_SIZE][BOARD_SIZE];
    private static Stage window;
    private Pane root = new Pane();

    /**
     * @param game current game being played.
     *
     * @return Parent root
     *
     */
    private Parent createContent(Game game) {
        root.setPrefSize(751, 751);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Tile tile = new Tile(i, j);
                tile.setTranslateX(j * 50);
                tile.setTranslateY(i * 50);

                tile.setOnMousePressed(e -> {
                    game.coordinates.setRow(tile.getTileRow());
                    game.coordinates.setCol(tile.getTileCol());
                });

                root.getChildren().add(tile);
                getTileBoard()[i][j] = tile;
            }
        }
        return root;
    }

    /**
     * @return  array matrix of tiles to represent game board.
     */
    Tile[][] getTileBoard() { return tileBoard; }


    /**
     * @param winner to load game winner on scene
     */
    private static void loadGameOver(String winner){
        GameOverMenu gameOver = new GameOverMenu();

        Scene gameOverScreen = new Scene(gameOver.loadGameOver(winner));

        window.setScene(gameOverScreen);

        window.setResizable(false);

        window.centerOnScreen();

        window.show();
    }


    /**
     * Loads main menu
     */
    static void loadMainMenu() {

        MainMenu mainmenu = new MainMenu();

        Scene mainMenu = new Scene(mainmenu.getMainMenu());

        window.setScene(mainMenu);

        window.setTitle("Gomoku");

        window.setResizable(false);

        window.centerOnScreen();

        window.show();
    }


    /**
     * @param opponent
     * 1: Human vs. Human. (default)
     * 2: Human vs. AI random play.
     * 3: Human vs. AI Minimax.
     */
    static void loadNewGame(int opponent) {

        Game game = new Game(15, 5, opponent, 4);
        Scene boardGUI = new Scene(game.GUI.createContent(game));

        Stage gameStage = window;
        gameStage.setTitle("Gomoku");
        gameStage.setScene(boardGUI);
        gameStage.setResizable(false);
        gameStage.centerOnScreen();
        gameStage.show();

        boardGUI.setOnMouseClicked(e -> {

            if (game.getOpponentType() == 1) {
                if (!game.getGameOver()) {
                    if (game.getDrawCount() % 2 == 0) game.playerMakeMove(game, 2);
                    else game.playerMakeMove(game, 1);
                    game.drawCountPlus1();
                }
            }

            // && game.getGameBoard().isMoveAvailable(game.coordinates.getRow(), game.coordinates.getCol())
            if (game.getOpponentType() == 2) {
                if (!game.getGameOver()) game.playerMakeMove(game, 2);
                if (!game.getGameOver()) game.makeMoveAIRandom(game);
            }

            if (game.getOpponentType() == 3) {
                if (!game.getGameOver()) game.playerMakeMove(game, 2);
                if (!game.getGameOver()) game.makeMoveMinimax(game, game.getDepth(), 1, false);
            }

            // Loads game over screen with game outcome
            if (game.getGameOver() || game.getDrawCount() == 225) {
                if (game.getDrawCount() == 225) loadGameOver("Draw");
                else {
                    String winner = game.getWinner() == 1 ? "White Wins" : "Black Wins";
                    loadGameOver(winner);
                }
            }
        });
    }


    /**
     * Loads application starting from main menu
     */
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        loadMainMenu();
    }


    /**
     * Each individual tile on game board with coordinates and methods.
     */
    class Tile extends StackPane {

        Text text = new Text();
        int row, column;

        Tile(int x, int y) {
            this.row = x;
            this.column = y;

            Rectangle border = new Rectangle(50, 50);
            border.setFill(Color.BURLYWOOD);
            border.setStroke(Color.BLACK);
            text.setFont(Font.font(40));
            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);
        }


        /**
         *
         * @param board board to play move on.
         * @param player player to play move for.
         * @param row row coordinate of move.
         * @param col column coordinate of move.
         *
         */
        void makeMoveGUI(Board board, final int player, final int row, final int col) {
            if (board.isMoveAvailable(row, col)) {
                drawTile(player);
                board.makeMoveMatrix(board, player, row, col);
            }
        }


        /**
         * @param player player to draw tile for.
         */
        private void drawTile(int player) {
            text.setText("O");
            if (player == 1) text.setFill(Color.WHITE);
            else text.setFill(Color.BLACK);
        }

        int getTileRow() {
            return row;
        }

        int getTileCol() {
            return column;
        }

    }
}
