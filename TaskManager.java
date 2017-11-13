import java.io.*;
import java.net.*;
import java.lang.Thread;

public class TaskManager implements Runnable {
    private Socket player1;
    private Socket player2;
	private Tile[][] map;
    private DataInputStream  input1;
    private DataInputStream  input2;
    private DataOutputStream output1;
    private DataOutputStream output2;
    
    
    TaskManager(Socket player1, Socket player2) throws Exception {
        this.player1 = player1;
        this.player2 = player2;
        input1  = new DataInputStream ( player1.getInputStream()  );
        input2  = new DataInputStream ( player2.getInputStream()  );
        output1 = new DataOutputStream( player1.getOutputStream() );
        output2 = new DataOutputStream( player2.getOutputStream() );
    }

	public TaskManager(DataInputStream in, DataOutputStream out, Socket socket2) throws Exception {
        this.player2 = socket2;
        input1  = in;
        input2  = new DataInputStream ( player2.getInputStream()  );
        output1 = out;
        output2 = new DataOutputStream( player2.getOutputStream() );

	}

	/* stuff stuff    
     * @param           
     * @return a value **/
    public void run() {
        /* stuff stuff */
        try{
            output1.writeUTF("Hello worldy world! GO PLAYER 1!!!");
            output2.writeUTF("Hello worldy world! GO PLAYER 2!!!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void setPosition(Ship boat, int x, int y) {

	}

	public boolean isWon() {
		return false;
	}

	public void sendUpdate() {
        
	}

    
}