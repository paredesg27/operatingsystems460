import java.net.ServerSocket;
import java.net.Socket;
//George Paredes CSC 460 Assingment 2

class Dispatcher {
    static ServerSocket port;

    public static void main(String[] args) {
        ServerThread currentThread;
        Socket currentSocket;
        try {
            port = new ServerSocket(9877);
        } catch (Exception e) {
            System.out.println("\nerror assigning port "+ e.getMessage());
        }

        while (true) {
            try {
                currentSocket = port.accept();
                currentThread = new ServerThread();
                currentThread.createSocket(currentSocket);
                currentThread.run();
            } catch (Exception e) {
                System.out.println("\nError creating ServerThread " + e.getMessage());
            }
        }
    }
}
