import java.io.BufferedReader;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//George Paredes CSC 460 Assingment 2

public class Client {
    private static String hostName = "localhost";
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Socket toSeverSocket;
    private static char[][] board;
    private static int row;
    private static int col;

    public static void main(String[] args) {
        // initialize all variables
        try {
            toSeverSocket = new Socket(hostName, 9877);
            inputStream = new DataInputStream(toSeverSocket.getInputStream());
            outputStream = new DataOutputStream(toSeverSocket.getOutputStream());
            out = new PrintWriter(outputStream, true);
            in = new BufferedReader(new InputStreamReader(inputStream));

        } catch (Exception e) {
            System.out.println("Error in connecting to server");
        }
        // connect to “localhost” on port 9877 to receive Socket object and store it in
        // toServerSocket
        // set up the various I/O objects for the Socket communication
        // next we will create an empty local board filled with blanks (can use a method
        // for this)
    

        board = new char[4][4];
        for (int x = 0; x <= 3; x++)
            for (int y = 0; y <= 3; y++)
                board[x][y] = ' ';
        row = -1;
        col = -1;
    

        // Here you should call a method playgame( ) and pass it the BufferedReader
        // and PrintWriter objects.
       

        try {
            playgame(in, out);
        } catch (Exception e) {
            System.out.println("Error calling playgame(): " + e.getMessage());
            e.printStackTrace();
        }
        // System.out.println("this works");
    } // end main( )

    public static void playgame(BufferedReader in, PrintWriter out) throws IOException {
        // Declare Scanner variable named inp to read from standard input/keyboard
        Scanner inp = new Scanner(System.in);
        // Declare a String named response to hold messages from the server
        String response;
        // Declare a boolean flag named turn initialized to false indicating server’s
        // turn to talk
        boolean turn = false;
        // Declare a second boolean variable named gameover which is set to false
        boolean gameover = false;


        while (gameover == false) {
            if (turn) { // it is user’s turn
                do {
                    System.out.print("\nEnter your move (row(0-3) column(0-3)): ");
                    // add code here to read line from standard input/keyboard, split it and parse
                    // the two ints
                    response = inp.nextLine();
                    // System.out.println("\nworks");

                    String[] data = response.split("\\s+");
                    row = Integer.parseInt(data[0]);
                    col = Integer.parseInt(data[1]);
                } while (row < 0 || row > 3 || col > 3 || col < 0 || board[row][col] != ' ');
                // The do-while condition above ensures that the move is LEGAL
                // Set the correct cell of board matrix to 'O'
                board[row][col] = 'O';
                // now send the move message to the server via the PrintWriter object’s println(
                // ) method
                // System.out.println("MOVE " + row + " " + col);
                out.println("MOVE " + row + " " + col);
                System.out.println("\n***********USER's turn****************\n");
                printboard();
                turn = false;
                // Be sure it is in the correct format MOVE row# col#
            } // end if
            else { 
                // must be servers move
                // read a message from BufferedReader object using readLine( ) method and store
                // it in
                // the response string
                response = in.readLine();
                // System.out.println(response);
           
                if (response.equals("CLIENT") == false) { // must be server's move
                // System.out.println(response);
                    String[] args = response.split("\\s+"); // this statement splits the message into tokens
                    // System.out.println("\ntest here");
                    // System.out.println(args[0]);
                    if (args.length > 3) {
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        if (args[3] != "WIN" && row != -1){
                            board[row][col] = 'X';
                        }
                            
                        switch (args[3]) {
                            case "WIN":
                                System.out.println("\n\nCongratulations!!! You WON the game!");
                                break;
                            case "TIE":
                                System.out.println("\nThe game was a TIE!");
                                break;
                            case "LOSS":
                                System.out.println("\nSORRY! You LOST the game!");
                                break;
                        } // end switch
                     
                        gameover = true;
                    } // end if
                    else // not more than 3 arguments were sent move was not a win, loss, or tie – just a
                         // regular old move
                    {
                        // System.out.println("\nhere in else");
                        System.out.println("\n***********Server's turn****************\n");
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        board[row][col] = 'X';
                        // get row from args[1]
                        // get col from args[2]
                        // set cell of board matrix to ‘X’
                        printboard();
                        turn = true;
                    } // end else
                } // end if - server’s move code ends
                else { // message must have been "CLIENT"
                    System.out.println("\nYOU MOVE FIRST");
                    turn = true;
                }
            } // end else
         
        } // end while loop for gameover
        inp.close();
        System.out.println("\n\nHere is the final game board\n");
        printboard();
    } // end playgame method

    static void printboard() {
        String boardTemplate = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                boardTemplate = boardTemplate + board[i][j] + " | ";
            }
            boardTemplate = boardTemplate + "\n----------------\n";
        }

   
        System.out.println(boardTemplate);
    }
} // end class Client