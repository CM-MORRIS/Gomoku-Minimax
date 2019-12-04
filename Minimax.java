import java.util.ArrayList;

class Minimax {

    private Board board;

    Minimax(Board board) {
        this.board = board;
    }


    /**
     * Finds best move for AI to play.
     *
     * @param depth depth of the tree to search for a move.
     * @param player player to search for best move for
     * @param forBlack true - black's turn, false - white's (AI) turn.
     *
     * @return the best move for AI to play as an int[]
     */
    final int[] getBestMove(final int depth, final int player, boolean forBlack) {

        int[] move = new int[2];

        // checks if there is a winning move for AI
        Object[] bestMove = isWinningMove(board, player, forBlack);

        // returns winning move if found
        if (bestMove != null) {
            move[0] = (Integer) (bestMove[1]);
            move[1] = (Integer) (bestMove[2]);
        }

        // if no winning move engage minimax search
        else {

            bestMove = minimaxAlphaBeta(board, !forBlack, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

            if (bestMove[1] == null) move = null;

            // converts best move from object to array
            else {
                move[0] = (Integer) (bestMove[1]);
                move[1] = (Integer) (bestMove[2]);
            }
        }
        return move;
    }


    /**
     * Evaluates board to return overall score
     *
     * @param board the board to evaluate.
     * @param blacksTurn depth of the tree to search for a move.
     *
     * @return final score as double
     *
     */
    private double evaluateBoard(final Board board, boolean blacksTurn) {

        double blackScore = heuristicEvaluation(board, true, blacksTurn);
        double whiteScore = heuristicEvaluation(board, false, blacksTurn);

        if (blackScore == 0) return whiteScore;
        if (whiteScore == 0) return blackScore;
        return whiteScore / blackScore;
    }


    /**
     * Evaluates board state for all axis' and returns a score.
     *
     * @param board the board to evaluate.
     * @param forBlack depth of the tree to search for a move.
     * @param blacksTurn depth of the tree to search for a move.
     *
     * @return evaluation score as integer.
     */
    private int heuristicEvaluation(final Board board, final boolean forBlack, boolean blacksTurn) {

        final int[][] boardMatrix = board.getBoardMatrix();
        return evaluateHorizontal(boardMatrix, forBlack, blacksTurn) +
                evaluateVertical(boardMatrix, forBlack, blacksTurn) +
                evaluateDiagonal(boardMatrix, forBlack, blacksTurn);
    }


    /**
     * Checks if there is a winning move on board for player.
     *
     * @param board the board to evaluate.
     * @param player player to check winning move for
     * @param forBlack if evaluating board for black or not. Parsed to heuristicEvaluation to check if win score met.
     *
     * @return Object array containing a winning move if found else null.
     */
    private Object[] isWinningMove(final Board board, int player, boolean forBlack) {
        ArrayList<int[]> availableMoves = board.getSpecificMoves();

        Object[] winningMove = new Object[3];
        final int IS_FIVE_IN_ROW = 200000000;

        // Iterate for all available moves
        for (int[] move : availableMoves) {

            // Create a temporary board that is equivalent to the current board
            Board boardCopy = new Board(board);

            // Play the move to boardCopy matrix
            boardCopy.makeMoveMatrix(boardCopy, player, move[0], move[1]);

            // If the player has a winning move, return the move.
            if (heuristicEvaluation(boardCopy, forBlack, forBlack) >= IS_FIVE_IN_ROW) {
                winningMove[1] = move[0];
                winningMove[2] = move[1];
                return winningMove;
            }
        }
        return null;
    }


    /**
     * Uses Minimax and alpha-beta pruning to search at a depth for best move to play for player.
     *
     * @param board the board to evaluate.
     * @param forBlack which player is searching for best move? black = true, white = false.
     * @param depth depth of tree to search.
     * @param alpha alpha score minimiser
     * @param beta beta score maximiser
     *
     * @return object array containing best move and its companion score
     *
     */
    private Object[] minimaxAlphaBeta(Board board, boolean forBlack, int depth, double alpha, double beta) {

        int player = forBlack ? 2 : 1;
        Object[] bestMove = new Object[3];
        ArrayList<int[]> availableMoves = board.getSpecificMoves();

        // ends recursive call
        if (depth == 0 || availableMoves.size() == 0) {
            return new Object[]{ evaluateBoard(board, !forBlack), null, null };
        }

        // if blacks turn
        if (!forBlack) {

            bestMove[0] = alpha;

            // Iterate for all possible generated moves
            for (int[] move : availableMoves) {

                // Create copy of parsed board
                Board boardCopy = new Board(board);

                // Uses temporary board to make moves
                boardCopy.makeMoveMatrix(boardCopy, player, move[0], move[1]);

                // Recurse to next depth of tree
                Object[] currentAlpha = minimaxAlphaBeta(boardCopy, true, depth - 1, alpha, beta);

                // Update alpha
                alpha = Math.max((double) currentAlpha[0], alpha);

                // Pruning with beta
                if ((double) (currentAlpha[0]) >= beta) return currentAlpha;

                // update to new move if necessary
                if ((double) currentAlpha[0] > (double) bestMove[0]) {
                    bestMove = currentAlpha;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        }

        // if not blacks turn
        else {

            bestMove[1] = availableMoves.get(0)[0];
            bestMove[2] = availableMoves.get(0)[1];
            bestMove[0] = beta;

            for (int[] move : availableMoves) {

                Board boardCopy = new Board(board);
                boardCopy.makeMoveMatrix(boardCopy, player, move[0], move[1]);

                // Recurse to next depth of tree
                Object[] currentBeta = minimaxAlphaBeta(boardCopy, false, depth - 1, alpha, beta);

                // update beta
                beta = Math.min((double) currentBeta[0], beta);

                //Pruning with alpha
                if ((double) (currentBeta[0]) <= alpha) return currentBeta;

                // update to new move if necessary
                if ((double) currentBeta[0] < (double) bestMove[0]) {
                    bestMove = currentBeta;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        }
        return bestMove;
    }


    /**
     * Searches and evaluates score for all horizontal axis on board.
     *
     * @param boardMatrix the board to evaluate.
     * @param forBlack which player evaluating board for? black = true, white = false.
     * @param blacksTurn true if blacks turn to move else false
     *
     * @return score for all horizontal as integer
     *
     */
    private int evaluateHorizontal(int[][] boardMatrix, boolean forBlack, boolean blacksTurn) {

        int player = forBlack ? 2 : 1;
        int consecutive = 0;
        int blocks = 2;
        int score = 0;
        int empty = 0;

        for (int[] row : boardMatrix) {
            for (int col = 0; col < boardMatrix[0].length; col++) {

                if (row[col] == player) consecutive++;
                else if (row[col] == empty) {

                    // block detected
                    if (consecutive != empty) {
                        blocks--;
                        score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);
                        consecutive = empty;
                        blocks = 1;
                    }
                    else blocks = 1;
                }
                else if (consecutive != empty) {
                    score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);
                    consecutive = empty;
                    blocks = 2;
                }
                else blocks = 2;
            }
            if (consecutive != empty) score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);

            consecutive = empty;
            blocks = 2;
        }
        return score;
    }


    /**
     * Searches and evaluates score for all vertical axis on board.
     *
     * @param boardMatrix the board to evaluate.
     * @param forBlack which player evaluating board for? black = true, white = false.
     * @param blacksTurn true if blacks turn to move else false
     *
     * @return score for all vertical as integer
     *
     */
    private int evaluateVertical(int[][] boardMatrix, boolean forBlack, boolean blacksTurn) {

        int player = forBlack ? 2 : 1;
        int consecutive = 0;
        int blocks = 2;
        int score = 0;
        int empty = 0;

        for (int row = 0; row < boardMatrix[0].length; row++) {
            for (int[] col : boardMatrix) {

                if (col[row] == player) consecutive++;
                else if (col[row] == empty) {

                    if (consecutive != empty) {
                        blocks--;
                        score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);
                        consecutive = empty;
                        blocks = 1;
                    }
                    else blocks = 1;

                }
                else if (consecutive != empty) {
                    score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);
                    consecutive = empty;
                }
                else blocks = 2;
            }
            if (consecutive != empty) score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);

            consecutive = empty;
            blocks = 2;
        }
        return score;
    }


    /**
     * Searches and evaluates score for all diagonal axis on board.
     *
     * @param boardMatrix the board to evaluate.
     * @param forBlack which player evaluating board for? black = true, white = false.
     * @param blacksTurn true if blacks turn to move else false
     *
     * @return score for all diagonals as integer
     *
     */
    private int evaluateDiagonal(int[][] boardMatrix, boolean forBlack, boolean blacksTurn) {

        int player = forBlack ? 2 : 1;
        int consecutive = 0;
        int blocks = 2;
        int score = 0;
        int empty = 0;

        // bottom left to top right
        for (int i = 0; i <= 2 * (boardMatrix.length - 1); i++) {

            int rowBegin = Math.max(0, i - boardMatrix.length + 1);
            int rowEnd = Math.min(boardMatrix.length - 1, i);

            for (int row = rowBegin; row <= rowEnd; ++row) {
                int col = i - row;

                if (boardMatrix[row][col] == player) consecutive++;

                else if (boardMatrix[row][col] == empty) {
                    if (consecutive != empty) {
                        blocks--;
                        score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);
                        consecutive = empty;
                        blocks = 1;
                    }
                    else blocks = 1;
                }
                else if (consecutive != empty) {
                    score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);
                    consecutive = empty;
                    blocks = 2;
                }
                else blocks = 2;
            }
            if (consecutive != empty) score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);

            consecutive = empty;
            blocks = 2;
        }

        // left to right diagonally
        for (int i = 1 - boardMatrix.length; i < boardMatrix.length; i++) {

            int rowBegin = Math.max(0, i);
            int rowEnd = Math.min(boardMatrix.length + i - 1, boardMatrix.length - 1);

            for (int row = rowBegin; row <= rowEnd; ++row) {
                int col = row - i;

                if (boardMatrix[row][col] == player) consecutive++;
                else if (boardMatrix[row][col] == empty) {
                    if (consecutive != empty) {
                        blocks--;
                        score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);
                        consecutive = empty;
                        blocks = 1;
                    }
                    else blocks = 1;
                }
                else if (consecutive != empty) {
                    score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);
                    consecutive = empty;
                    blocks = 2;
                }
                else blocks = 2;
            }
            if (consecutive != empty) score += getEvaluationScore(consecutive, blocks, forBlack == blacksTurn);

            consecutive = empty;
            blocks = 2;
        }
        return score;
    }


    /**
     * Evaluates board state.
     *
     * @param count how many consecutive stones found.
     * @param blocks how many closed ends are at the ends of the found consecutive stones; 1, 2 or 0.
     * @param blacksTurn if evaluating for black or white
     *
     * @return evaluation score as integer.
     */
    private int getEvaluationScore(final int count, final int blocks, final boolean blacksTurn) {

        boolean OPEN_ENDS = blocks == 0;
        boolean ONE_BLOCK = blocks == 1;
        boolean TWO_BLOCK = blocks == 2;

        final int WIN = 200000000;

        final int B_FOUR_ROW_TWO_OPEN = 2000000;
        final int W_FOUR_ROW_TWO_OPEN = 500000;

        final int B_FOUR_ROW_ONE_OPEN = 1900000;
        final int W_FOUR_ROW_ONE_OPEN = 700;

        final int B_FOUR_ROW_BLOCKED = 1000000;
        final int W_FOUR_ROW_BLOCKED = 350;

        final int B_THREE_ROW_TWO_OPEN = 100000;
        final int W_THREE_ROW_TWO_OPEN = 400;

        final int B_THREE_ROW_ONE_OPEN = 20;
        final int W_THREE_ROW_ONE_OPEN = 5;

        final int B_THREE_ROW_BLOCKED = 10;
        final int W_THREE_ROW_BLOCKED = 5;

        final int B_TWO_ROW_TWO_OPEN = 14;
        final int W_TWO_ROW_TWO_OPEN = 12;

        final int B_TWO_ROW_ONE_OPEN = 6;
        final int W_TWO_ROW_ONE_OPEN = 6;

        final int B_TWO_ROW_BLOCKED = 3;
        final int W_TWO_ROW_BLOCKED = 3;

        final int ONE_STONE = 1;


        if (blocks == 2 && count < 5) return 0;

        switch (count) {
            case 5:
                return WIN;

            case 4:
                if (OPEN_ENDS) {
                    if (blacksTurn) return B_FOUR_ROW_TWO_OPEN;
                    else return W_FOUR_ROW_TWO_OPEN;
                }
                if (ONE_BLOCK) {
                    if (blacksTurn) return B_FOUR_ROW_ONE_OPEN;
                    else return W_FOUR_ROW_ONE_OPEN;
                }
                // double blocked
                else if (TWO_BLOCK) {
                    if (blacksTurn) return B_FOUR_ROW_BLOCKED; // was 1000000
                    else return W_FOUR_ROW_BLOCKED;
                }

            case 3:
                if (OPEN_ENDS) {
                    if (blacksTurn) return B_THREE_ROW_TWO_OPEN;
                    else return W_THREE_ROW_TWO_OPEN;
                }
                if (ONE_BLOCK) {
                    if (blacksTurn) return B_THREE_ROW_ONE_OPEN;
                    else return W_THREE_ROW_ONE_OPEN;
                }

                // double blocked
                else if (TWO_BLOCK) {
                    if (blacksTurn) return B_THREE_ROW_BLOCKED;
                    else return W_THREE_ROW_BLOCKED;
                }

            case 2:
                if (OPEN_ENDS) {
                    if (blacksTurn) return B_TWO_ROW_TWO_OPEN;
                    else return W_TWO_ROW_TWO_OPEN;

                } else if (ONE_BLOCK) {
                    if (blacksTurn) return B_TWO_ROW_ONE_OPEN;
                    else return W_TWO_ROW_ONE_OPEN;

                } else if (TWO_BLOCK) {
                    if (blacksTurn) return B_TWO_ROW_BLOCKED;
                    else return W_TWO_ROW_BLOCKED;
                }

            case 1:
                return ONE_STONE;
        }
        return 0;
    }
}