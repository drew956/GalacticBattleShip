import java.io.*;
import java.net.*;
import java.lang.Thread;

public class TaskManager implements Runnable {
    private Socket player1;
    private Socket player2;
	private Tile[][] map;
    private ObjectInputStream  obInput1;
    private ObjectInputStream  obInput2;
    private ObjectOutputStream obOutput1;
    private ObjectOutputStream obOutput2;
    private Ship[][] ships;
    private int tilesX = 20;
    private int tilesY = 20; //default size of the map
    
    TaskManager(Socket player1, Socket player2) throws Exception {
        this.player1 = player1;
        this.player2 = player2;
        obInput1  = new ObjectInputStream ( player1.getInputStream()  );
		obInput2  = new ObjectInputStream ( player2.getInputStream()  );
		obOutput1 = new ObjectOutputStream( player1.getOutputStream() );
		obOutput2 = new ObjectOutputStream( player2.getOutputStream() );
		obOutput1.reset();
		obOutput2.reset();
		obOutput1.writeInt(0);
		obOutput1.flush();
		obOutput2.writeInt(1);
		obOutput2.flush();
		initializeShips();
    }

	public TaskManager(ObjectOutputStream out, ObjectOutputStream out2, Socket socket, Socket socket2) throws Exception {
		this.player1 = socket;
		this.player2 = socket2;
        obOutput1 = out;
        obOutput2 = out2; //new DataOutputStream( player2.getOutputStream() );
		obInput1  = new ObjectInputStream ( player1.getInputStream()  );
		obInput2  = new ObjectInputStream( player2.getInputStream() );
        initializeShips();

	}

    public void run() {
    	while(true){
	        try{
	        	obOutput1.writeObject(ships); //this is how we know whose turn it is: it always starts by sending the map to P1
	        	obOutput1.reset();
	        	ships = (Ship[][]) obInput1.readObject();
	        }catch (Exception e){
	            System.out.println(e.getMessage());
	            try {
					obOutput1.close();
					obInput1.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	            break;
	        }
	        
	    	try {
				obOutput2.writeObject(ships);
	        	obOutput2.reset();
		    	ships = (Ship[][]) obInput2.readObject();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					obOutput2.close();
					obInput2.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			} 
    	}
    }
    
    public void setPosition(Ship boat, int x, int y) {

	}

	public boolean isWon() {
		return false;
	}

	public void sendUpdate() {
        
	}
	private void initializeShips(){
		ships = new Ship[tilesX][tilesY];
		for(int i = 0; i < tilesX; i++){
			for(int j = 0; j < tilesY; j++){
				ships[i][j] = null;
				//fleet[i][j] = new Ship( j < tilesY / 2 ? 0 : 1);
			}
		}
		
		for(int i = 0; i < ships[0].length; i += 3){
			ships[i][0] = new Ship(0);
			ships[i][ships[0].length - 1] = new Ship(1);
		}
	}


    
}