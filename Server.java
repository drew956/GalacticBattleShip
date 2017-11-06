import java.io.*;
import java.net.*;
import java.lang.Thread;

class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8000);
                
        while(true){
            Socket socket  = server.accept(); //player 1
            Socket socket2 = server.accept(); //player 2

            TaskManager task = new TaskManager(socket, socket2);
            new Thread(task).start();
        }
        
    }

}