import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.lang.Thread;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/* Tutorial on game development using javafx, useful for creating the game loop
 * https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
 * 
 * */
public class Client extends Application {

	private Tile[][] map;
	private Ship[][] fleet;
	private Player   player;
	private boolean myTurn;
	private boolean baseStatus;
	private Socket  server;
	private DataInputStream  input;
	private DataOutputStream output;
	private Stage primaryStage;
	private ArrayList<Scene> sceneArray = new ArrayList<Scene>();
	
	@Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PrimaryStage!");
        
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10));
        pane.setHgap(10.0);
        pane.setVgap(10.0);
        
        final TextField ipOfServer = new TextField();
        final  TextArea   textArea = new TextArea();
		final  TextArea       text = new TextArea();
        final TextField       port = new TextField();

        Label portLabel = new Label("Enter the port number");
		Label   ipLabel = new Label("Enter the ip address");

        port.setText("8000");
		ipOfServer.setText("localhost");
		textArea.setDisable(true);
		
		
		textArea.setEditable(false);
        text.setEditable(false);
        
        Button btn = new Button();
        btn.setText("Connect to server");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            public void handle(ActionEvent event) {
                try {
                	Socket server = new Socket(
                			ipOfServer.getText(),  
                			Integer.parseInt(port.getText())
                	);
                	DataInputStream input = new DataInputStream(
                			server.getInputStream()
                	);
                	DataOutputStream output = new DataOutputStream(
                			server.getOutputStream()
                	);
                	/*
                	 * For simulating two connections
                	Socket server2 = new Socket(
                			ipOfServer.getText(),  
                			Integer.parseInt(port.getText())
                	);*/
                    String data = input.readUTF();
                    textArea.appendText(data + "\n");
                    server.close();
                } catch( Exception e ){
                    textArea.appendText(e + "\n");
                }
            }
            
        });
        pane.add(portLabel, 0, 0);
        pane.add(port, 0, 1);
        pane.add(ipLabel, 1, 0);
        pane.add(ipOfServer, 1, 1);
        pane.add(btn, 0, 3);
        pane.add(textArea, 0, 2);
        
        sceneArray.add(new Scene(pane, 300, 250));
        primaryStage.setScene(sceneArray.get(0));
        primaryStage.setWidth(700.0);
        primaryStage.setHeight(350.0);
        primaryStage.show();
        this.primaryStage = primaryStage;
        
        initScenes();
    }
	
	public void stop() {
		if(server != null){
			try { 
				server.close();
			} catch(Exception e){
				System.out.println(e);				
			}
		}
	}
	
    public static void main(String[] args) {
    	System.out.println("Called main");
        launch(args);
    }
    
    
    /*
    public void run() {
        try {
            String data = input.readUTF();
            System.out.println(data);        
        } catch( Exception e ){
            System.out.println(e);
        }
	} */
    
    private void connectToServer(){
        String host = "localhost";
        
        try {
            // Create a socket to connect to the server
            
            server = new Socket(host, 8000);

            input = new DataInputStream(server.getInputStream());

            // Create an output stream to send data to the server
            output = new DataOutputStream(server.getOutputStream());
            /*ClientTaskManager task = new ClientTaskManager(server);
            Thread t = new Thread(task);
            t.start();*/
        }
        catch (Exception ex) {
            System.err.println(ex);
        }

        /*	Control the game on a separate thread
        	Thread thread = new Thread(this);
        	thread.start(); 
        */
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
		
	public void initScenes() {
		Canvas canvas = new Canvas(600, 800);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.ALICEBLUE );
		gc.setStroke(Color.GREEN);
		gc.strokeText("HELLO WORLD", 40.0, 30.0);
		GridPane pane = new GridPane();
		pane.add(canvas, 0, 0);
		
		Scene canvasScene = new Scene(pane, 600, 800);
		
		primaryStage.setScene(canvasScene);
	}
	

}
