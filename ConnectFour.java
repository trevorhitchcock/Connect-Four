import java.util.Scanner;

public class ConnectFour {
    public static void main(String[] args) {
        System.out.println("Welcome to Connect Four!");

        // creates ConnectFourBoard object with 6 rows and 7 columns
        ConnectFourBoard board = new ConnectFourBoard(6, 7);
        board.printBoard();

        // creates CPUConnectFour object with the initial board
        CPUConnectFour cpuConnectFour = new CPUConnectFour(board);

        Scanner scan = new Scanner(System.in); // creates scanner object for user input

        boolean isUser = true; // keeps track of whose move it is
        boolean gameComplete = false;

        do{
            // gets user move
            int move = -1;
            if(isUser) {
                System.out.println("Enter the column you'd like to play:");
                // type in 1-7 but move in program is between 0-6 to align with array indexing
                move = Integer.parseInt(scan.nextLine()) - 1;
            } else{
                System.out.println("CPU thinking...");
                move = cpuConnectFour.makeCPUMove(); // CPU makes a move using minimax algorithm
            }

            // valid input
            if(move > -1 && move < 7){
                if(board.dropDisc(move,isUser)){ // valid move
                    // check if game was won on the move
                    if(board.didWin(move, isUser)){
                        System.out.println("WINNER!");
                        board.printBoard();
                        break;
                    }

                    // check if the board is full
                    if(board.isBoardFull()){
                        gameComplete = true;
                        System.out.println("The board is full");
                        break;
                    }
                    isUser = !isUser; // changes turn from user to CPU
                }
                board.printBoard();
            } else{
                System.out.println("Invalid move! Enter a number between 1 and 7");
            }

        }while(!gameComplete); // while game isn't complete

    }
}