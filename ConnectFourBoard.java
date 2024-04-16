public class ConnectFourBoard {
    private final int[][] board;

    public ConnectFourBoard(int rows, int cols) {
        board = new int[rows][cols];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = 0;
            }
        }
    }

    public int[][] getBoard() {
        // returns a copy of the board to avoid direct modification of the original board
        int[][] copyBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copyBoard[i], 0, board[i].length);
        }
        return copyBoard;
    }

    public void printBoard() {
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    public boolean didWin(int move, boolean isUser){
        // stores location of last move and whether is was user or CPU
        int[] location = new int[2];

        for(int i =0; i< board.length; i++){
            if(board[i][move] != 0){ // last move found
                location[0] = i; // row
                location[1] = move; // column
                break;
            }
        }
        // names for readability
        int curRow = location[0];
        int curCol = location[1];

        // determines whether checking for 1s or 2s
        int numToCheck = isUser ? 1 : 2;

        // checks column for winner
        int curRun = 0; // finds longest run of numToCheck in column of move
        for(int i = board.length-1; i >= 0; i--){
            if(board[i][curCol] == numToCheck){ // if current spot has numToCheck
                curRun++;
                if(curRun == 4){ // person with last move won
                    return true;
                }
            }else{ // reset run
                curRun = 0;
            }
        }

        // checks row for winner
        curRun = 0;
        // for every element in the row of the last move
        for(int i = board[curRow].length -1; i >= 0; i--){
            if(board[curRow][i] == numToCheck){ // if current spot has numToCheck
                curRun++;
                if(curRun == 4){ // person with last move won
                    return true;
                }
            }else{ // reset run
                curRun = 0;
            }
        }

        // check diagonals
        // diagonal 1: from top-left to bottom-right
        int diagonal1StartRow = curRow - Math.min(curRow, curCol);
        int diagonal1StartCol = curCol - Math.min(curRow, curCol);
        curRun = 0;
        for (int i = diagonal1StartRow, j = diagonal1StartCol; i < board.length && j < board[0].length; i++, j++) {
            if (board[i][j] == numToCheck) {
                curRun++;
                if (curRun == 4) {
                    return true;
                }
            } else {
                curRun = 0;
            }
        }

        // diagonal 2: from top-right to bottom-left
        int diagonal2StartRow = curRow - Math.min(curRow, board[0].length - 1 - curCol);
        int diagonal2StartCol = curCol + Math.min(curRow, board[0].length - 1 - curCol);
        curRun = 0;
        for (int i = diagonal2StartRow, j = diagonal2StartCol; i < board.length && j >= 0; i++, j--) {
            if (board[i][j] == numToCheck) {
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

    public boolean isBoardFull(){
        // if any zeroes in top row, return false
        for(int i =0; i< board[0].length + 1; i++){
            if(board[0][i]==0){
                return false;
            }
        }
        // otherwise return true
        return true;
    }

    public boolean dropDisc(int column, boolean isUser) {
        // variable is called lowest but acts like a highest
        int lowest = -1; // lowest row available to drop disc

        // finds lowest row to place disc
        for (int[] row : board) {
            if (row[column] == 0) {
                lowest++;
            } else {
                break;
            }
        }

        if (lowest == -1) { // unsuccessful move
            System.out.println("This column is full! Try again.");
            return false;
        } else { // valid move, places disc
            // if isUser is true, discValue is 1, else 2
            int discValue = isUser ? 1 : 2;
            board[lowest][column] = discValue;
            return true;
        }
    }
}