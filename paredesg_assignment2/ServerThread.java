
import java.net.Socket;
import java.io.*;
import java.util.Random;
class ServerThread implements Runnable{

    private Socket toclientsocket; // the Socket used to communicate with the client
    private DataInputStream instream; // stores the input stream of the socket
    private DataOutputStream outstream; // stores the output stream of the socket
    private PrintWriter out; // the printwriter will allow us to use print( ) and println( ) methods
    private BufferedReader in; // the BufferedReader will allow us to use readLine( ) method
    private Random rand; // used to select random moves
    private char[][] board; // I implemented my game board as a matrix of char to make
    private int row, col; // Obviously to hold the current row and/or column values for move



    public void createSocket(Socket inputSocket){
        try {
            toclientsocket = inputSocket;
            rand = new Random();
            instream = new DataInputStream(toclientsocket.getInputStream());
            outstream = new DataOutputStream(toclientsocket.getOutputStream());
            out = new PrintWriter(outstream, true);
            in = new BufferedReader(new InputStreamReader(instream));
            board = new char[4][4];
           
            for(int j = 0; j< board.length; j++){
                for(int i = 0; i <board.length; i++){
                    board[j][i]= ' ';
                }
            }
            row = -1;
            col = -1;
        } catch (Exception e) {
            System.out.println("Error connecting to socket" + e.getMessage());
        }
    }

    public void run() {
        int counter = 0;
        String response = " ";
        boolean gameover = false;
        boolean turn;
        
        if(rand.nextInt(20) % 2 == 0){
            turn = false; 
        }
        else{
            turn = true;
           out.println("CLIENT");
        } 
       

        while(gameover == false){
            if(turn){//user turn
                try {
                    response = in.readLine();
                } catch (Exception e) {
                   System.out.println("\nSome sort of read error on socket in server thread" + e.getMessage());
                }

                String[] data = response.split("\\s+");
                row = Integer.parseInt(data[1]);
                col = Integer.parseInt(data[2]);

                board[row][col] = 'O';
                printboard();//create method
                counter++;
                
                if(checkwin() || counter == 16){
                    if(checkwin() == true)//create method
                {
                    out.println("MOVE -1 -1 WIN");
                    gameover = true;

                }
                else{
                     out.println("MOVE -1 -1 TIE");
                     gameover = true;
                } 
                }
               turn = false;
            }
            else{//server turn
                    System.out.println("\n***SERVER MOVE*****");
                    makemove();//create method
                    counter++;
                    board[row][col] = 'X';
                    printboard();
                if(checkwin() || counter == 16){
                    if(checkwin()){
                        out.println("MOVE " + row + " " + col + " LOSS");
                        gameover = true;
                    }
                    else{
                        out.println("MOVE " + row + " " + col + " TIE");
                        gameover = true;
                    }
                }
                else{
                    out.println("MOVE " + row + " " + col);
                    System.out.println("\nMOVE " + row + " " + col);
                }
                turn = !turn;
            }
        }   
    }

    void makemove(){
        do {
            row = rand.nextInt(4);
            col = rand.nextInt(4);
        } while (board[row][col] != ' ' || board[row][col] == 'O' || board[row][col] == 'X');
        }

     boolean checkwin(){
        for(int x = 0; x <= 3; x++){
            if(board[x][0]== board[x][1] && board[x][1] == board[x][2] &&
            board[x][2] == board[x][3] && board[x][0] != ' '){
                return true;
            }
            if(board[0][x] == board[1][x] && board[1][x] == board[2][x] &&
            board[2][x] == board[3][x] && board[0][x] != ' '){
                return true;
            }
        }
        if(board[0][0]== board[1][1] && board[1][1] == board[2][2] &&
        board[2][2] == board[3][3] && board[0][0] != ' '){
            return true;
        }
        if(board[0][3]== board[1][2] && board[1][2] == board[2][1] &&
        board[2][1] == board[3][0] && board[0][3] != ' '){
            return true;
        }
        return false;
    }

    void printboard(){
        String  boardTemplate = "";
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                boardTemplate = boardTemplate + board[i][j] + " | ";
            }
            boardTemplate = boardTemplate + "\n-------------\n";
        }
        System.out.println(boardTemplate);
    }
}