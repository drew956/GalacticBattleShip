import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;

/* Tutorial on game development using javafx, useful for creating the game loop
 * https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
 * 
 * */

public class Client extends Application {
	private Ship[][] fleet;
	private Player   player;
	private int      playerID;
	private boolean myTurn;
	private boolean baseStatus;
	private Socket  server;
	private ObjectInputStream input;
	private ObjectOutputStream output;
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
                	server = new Socket(
                			ipOfServer.getText(),  
                			Integer.parseInt(port.getText())
                	);
                	input = new ObjectInputStream(
                			server.getInputStream()
                	);
                	output = new ObjectOutputStream(
                			server.getOutputStream()
                	);
                	
                	System.out.println("About to get int");
                	String msg = input.readUTF();
                	playerID = input.readInt(); 
                	System.out.println("Player ID set to " + playerID);
                    initScenes(); //moved to here, because Animation Timer loop will start and may cause lag
                	setScreen(sceneArray.size() - 1);
                	textArea.appendText(msg);
                	textArea.appendText("" + playerID);

                } catch( Exception e ){
                    textArea.appendText(e + "\n");
                    e.printStackTrace();
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
		
	public void initScenes() {
		initGameScene();
	}

	private void initGameScene() {
		int size_x = 600;
		int size_y = 635;
		
		int tilesX = 20; //number of tiles in the x direction
		int tilesY = 20; //number of tiles (height of Tile[][] map)
		Canvas canvas = new Canvas(size_x, size_y);
		final CanvasManager manager = new CanvasManager(canvas, primaryStage);
		manager.setPlayerID(playerID); //by the time this is called, playerID is already set
		
	    final long startNanoTime = System.nanoTime();//System.nanoTime();
	    System.out.println("About to create animation timer");
	    new AnimationTimer()
	    {
	    	final static private int WAITING = 0;
	    	final static private int DOING_TURN = 1;
	    	final static private int DONE_TURN = 2;

	    	private int status = WAITING;//default
	        public void handle(long currentNanoTime)
	        {
	            double t = (currentNanoTime - startNanoTime) / 1000000000.0; //time in seconds 

	            switch(status){
	            	case WAITING:
	            		try{
	            			Ship[][] ships = (Ship[][]) input.readObject();
	            			manager.setFleet(ships);
	            			manager.setTurn();
		            		status = DOING_TURN;
	            		}catch(Exception e){
	            			System.out.println("ERROR OCCURRED IN animation timer");
	            			e.printStackTrace();
	            			manager.setFleet(null);
	            		}
	            		break;
	            	case DOING_TURN:
	            		if(manager.doneWithTurn()){
		           			status = DONE_TURN;
	            		}
	            		break;
	            	case DONE_TURN:
	            		try{
	            			output.reset();
	            			output.writeObject(manager.getShips());
	            			output.flush();
	            		}catch(Exception e){
	            			e.printStackTrace();
	            		}
	            		status = WAITING;
	            		break;
	            }
	            if(t > 1.0) {
	            	manager.drawMap();
        		}
	        }
	    }.start();
	    System.out.println("Just started the animation timer");
		
	    BorderPane pane = new BorderPane();
		pane.setCenter(canvas);
		pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		Scene newScene = new Scene(pane, size_x, size_y);
		sceneArray.add(newScene);

		primaryStage.setMinWidth(newScene.getWidth()+15);
		primaryStage.setMinHeight(newScene.getHeight());
	}

	/**
	 * Changes the current screen to a new one stored inside of the private primaryStage attribute
	 * Checks to make sure the scene requested is within bounds
	 * 
	 * **/
	private void setScreen(int i) {
		if(i < sceneArray.size()){
			primaryStage.setWidth(sceneArray.get(i).getWidth());
			primaryStage.setHeight(sceneArray.get(i).getHeight());
			primaryStage.setScene(sceneArray.get(i));
			primaryStage.setTitle("Scene " + sceneArray.size()); //can create a map <Integer, String> for titles
		} else {
			System.out.println("setScreen(int i): You've entered an invalid index for your scene.\n");
		}
	}
}
