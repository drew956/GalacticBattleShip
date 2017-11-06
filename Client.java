import java.io.*;
import java.net.*;
import java.lang.Thread;

public class Client implements Runnable {
	//private Tile[][] map;
	//private Ship[][] fleet;
	//private Player   player;
	private boolean myTurn;
	private boolean baseStatus;
	private DataInputStream input;
	private DataOutputStream output;

    public static void main(String[] args) {
        new Client();
    }
    Client(){
        connectToServer();
    }
    public void run() {
        try {
            String data = input.readUTF();
            System.out.println(data);        
        } catch( Exception e ){
            System.out.println(e);
        }
	}
    private void connectToServer(){
        String host = "localhost";
        
        try {
            // Create a socket to connect to the server
            Socket socket;
            socket = new Socket(host, 8000);

            input = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            output = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception ex) {
            System.err.println(ex);
        }

        // Control the game on a separate thread
        Thread thread = new Thread(this);
        thread.start();
    }
	public boolean selectShip(int x, int y) {
		return false;
	}

	/*
	public boolean moveShip(Ship boat, int x1, int y1) {
		return false;
	}

	public boolean attackShip(Ship attackingBoat, int x, int y) {
		return false;
	}
    */
	public void sendToServer() {

	}

	public void getFromServer() {

	}

	public void checkBaseStatus() {

	}

	public void waitForOwnTurn() {

	}

}
