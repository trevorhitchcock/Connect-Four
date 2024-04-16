public class CPUConnectFour {
    private final ConnectFourBoard board;
    private int maxDepth; // Maximum depth for minimax search

    public CPUConnectFour(ConnectFourBoard board) {
        this.board = board;
        // You can set the maximum depth for minimax here
        this.maxDepth = 6; // Adjust the depth as needed for performance
    }

    public boolean didWin(int[][] curGameBoard, int move, boolean isUser) {
        // Stores location of last move and whether it was made by the user or CPU
        int[] location = new int[2];

        // Finding the row where the disc was placed
        for (int i = 0; i < curGameBoard.length; i++) {
            if (curGameBoard[i][move] != 0) {
                location[0] = i; // Row
                location[1] = move; // Column
                break;
            }
        }

        // Row and column indices for the last move
        int curRow = location[0];
        int curCol = location[1];

        // Determines whether to check for 1s or 2s (user or CPU)
        int numToCheck = isUser ? 1 : 2;

        // Check column for a winner
        int curRun = 0;
        for (int i = curGameBoard.length - 1; i >= 0; i--) {
            if (curGameBoard[i][curCol] == numToCheck) {
                curRun++;
                if (curRun == 4) {
                    return true;
                }
            } else {
                curRun = 0;
            }
        }

        // Check row for a winner
        curRun = 0;
        for (int i = curGameBoard[curRow].length - 1; i >= 0; i--) {
            if (curGameBoard[curRow][i] == numToCheck) {
                curRun++;
                if (curRun == 4) {
                    return true;
                }
            } else {
                curRun = 0;
            }
        }

        // Check diagonals
        // Diagonal 1: from top-left to bottom-right
        int diagonal1StartRow = curRow - Math.min(curRow, curCol);
        int diagonal1StartCol = curCol - Math.min(curRow, curCol);
        curRun = 0;
        for (int i = diagonal1StartRow, j = diagonal1StartCol; i < curGameBoard.length && j < curGameBoard[0].length; i++, j++) {
            if (curGameBoard[i][j] == numToCheck) {
                curRun++;
                if (curRun == 4) {
                    return true;
                }
            } else {
                curRun = 0;
            }
        }

        // Diagonal 2: from top-right to bottom-left
        int diagonal2StartRow = curRow - Math.min(curRow, curGameBoard[0].length - 1 - curCol);
        int diagonal2StartCol = curCol + Math.min(curRow, curGameBoard[0].length - 1 - curCol);
        curRun = 0;
        for (int i = diagonal2StartRow, j = diagonal2StartCol; i < curGameBoard.length && j >= 0; i++, j--) {
            if (curGameBoard[i][j] == numToCheck) {
                curRun++;
                if (curRun == 4) {
                    return true;
                }
            } else {
                curRun = 0;
            }
        }

        return false;
    }

    public boolean isBoardFull(int[][] curGameBoard) {
        // Check if any zeroes exist in the top row
        for (int i = 0; i < curGameBoard[0].length; i++) {
            if (curGameBoard[0][i] == 0) {
                return false;
            }
        }
        // If no zeroes found in the top row, the board is full
        return true;
    }

    public int[][] dropDisc(int[][] curGameBoard, int column, boolean isUser) {
        int discValue = isUser ? 1 : 2;
        int lowest = -1; // Lowest available row to drop disc

        // Finds lowest empty row to place disc
        for (int i = curGameBoard.length - 1; i >= 0; i--) {
            if (curGameBoard[i][column] == 0) {
                lowest = i;
                break;
            }
        }

        if (lowest != -1) { // Valid move, places disc
            curGameBoard[lowest][column] = discValue;
        }

        return curGameBoard;
    }

    private boolean isBoardChanged(int[][] board1, int[][] board2) {
        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1[0].length; j++) {
                if (board1[i][j] != board2[i][j]) {
                    return true; // There's a change in the boards
                }
            }
        }
        return false; // Both boards are the same
    }

    private int[][] cloneBoard(int[][] originalBoard) {
        int[][] clonedBoard = new int[originalBoard.length][originalBoard[0].length];
        for (int i = 0; i < originalBoard.length; i++) {
            System.arraycopy(originalBoard[i], 0, clonedBoard[i], 0, originalBoard[i].length);
        }
        return clonedBoard;
    }

    // Minimax algorithm implementation
    private int minimax(int[][] curGameBoard, int move, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        // Terminal conditions - check if CPU won, if User won, or if board full
        if (didWin(curGameBoard, move, false)) {
            System.out.println("CPU won");
            return 1;
        }
        if (didWin(curGameBoard, move, true)) {
            System.out.println("User won");
            return -1;
        }
        if (isBoardFull(curGameBoard) || depth == maxDepth) {
            return 0;
        }

        if (isMaximizingPlayer) {
            // Maximizer's turn
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < curGameBoard[0].length; i++) {
                if (curGameBoard[0][i] == 0) { // Check if column is not full
                    int[][] updatedBoard = cloneBoard(curGameBoard);
                    updatedBoard = dropDisc(updatedBoard, i, isMaximizingPlayer);
                    int score = minimax(updatedBoard, i, depth + 1, alpha, beta, false);

                    bestScore = Math.max(bestScore, score);
                    alpha = Math.max(alpha, bestScore);

                    // Alpha-beta pruning
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestScore;
        } else {
            // Minimizer's turn
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < curGameBoard[0].length; i++) {
                if (curGameBoard[0][i] == 0) { // Check if column is not full
                    int[][] updatedBoard = cloneBoard(curGameBoard);
                    updatedBoard = dropDisc(updatedBoard, i, isMaximizingPlayer);
                    int score = minimax(updatedBoard, i, depth + 1, alpha, beta, true);

                    bestScore = Math.min(bestScore, score);
                    beta = Math.min(beta, bestScore);

                    // Alpha-beta pruning
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestScore;
        }
    }

    public int makeCPUMove() {
        int bestScore = -1000;
        int bestMove = -1;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        int[][] curGameBoard = board.getBoard(); // Access the current game board

        // Check each possible move
        for (int move = 0; move < curGameBoard[0].length; move++) {
            if (curGameBoard[0][move] == 0) { // Make sure column isn't full
                int[][] updatedBoard = cloneBoard(curGameBoard); // Clone the board for simulation
                updatedBoard = dropDisc(updatedBoard, move, false); // Simulate CPU move

                int score = minimax(updatedBoard, move, 0, alpha, beta, true); // Call minimax method

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
            System.out.println("Best move: "+bestMove+", Best score: "+bestScore);
        }

        return bestMove; // Return the column with the best move
    }
}

