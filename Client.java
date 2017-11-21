import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.lang.Thread;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.animation.Animation.Status;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

/* Tutorial on game development using javafx, useful for creating the game loop
 * https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
 * 
 * */

public class Client extends Application {
	private Tile[][] map;
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
    
    
    /*
    public void run() {
        try {
            String data = input.readUTF();
            System.out.println(data);        
        } catch( Exception e ){
            System.out.println(e);
        }
	} */
    @Deprecated
    private void connectToServer(){
        String host = "localhost";
        
        try {
            // Create a socket to connect to the server
            
            server = new Socket(host, 8000);

            DataInputStream input = new DataInputStream(server.getInputStream());

            // Create an output stream to send data to the server
            DataOutputStream output = new DataOutputStream(server.getOutputStream());
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
		//initGameScene(); //putting each scene into its own function will allow us to tweak them easier
		//initTestScene();
		initTestScene2();
	}
	/**
	 * <p>Initializes the main game's Screen object
	 * The initial idea was to use a Canvas, but using nodes is simpler.<br />
	 * javafx has a nifty TranslateTransition class that can do animation
	 * for us as well </p>
	 **/
	private void initGameScene() {
		Canvas canvas = new Canvas(600, 600);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setFill(Color.BEIGE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setFill(Color.ALICEBLUE );
		gc.setStroke(Color.GREEN);
		gc.strokeText("HELLO WORLD", 40.0, 30.0);
		
		gc.setFill(Color.CORNSILK);
		gc.fillOval(canvas.getWidth()/2 - 40/2, canvas.getHeight()/2 - 60/2, 40, 60);

		gc.setFill(Color.MAROON);
		gc.fillPolygon( new double[] {100.0, 150.0, 100.0, 50.0}, new double[] {50.0, 100.0, 150.0, 100.0}, 4);
		gc.strokeOval(canvas.getWidth()/2 - 40/2, canvas.getHeight()/2 - 60/2, 40, 60);
		
		
		
		GridPane pane = new GridPane();
		pane.add(canvas, 0, 0);

		//this is for figuring out where things are positioned
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>(){

			public void handle(MouseEvent event) {
				System.out.println(event.getX());
				System.out.println(event.getY());
			}
			
			
		});
		Scene canvasScene = new Scene(pane, canvas.getWidth(), canvas.getHeight());
		sceneArray.add(canvasScene);

		primaryStage.setWidth(canvasScene.getWidth());  //have to resize the stage before
		primaryStage.setHeight(canvasScene.getHeight()); //changing the root, else it will not resize properly
		primaryStage.setScene(canvasScene);
	}

	/** 
	 * <i>initTestScene</i> is currently being used to test if using a normal
	 * GridPane or other pane would be more effective than using a canvas <br />
	 * Upon further testing I have concluded that this will be far easier to use than the Canvas element
	 * 
	 * TranslateTransitions are dope:
	 * https://docs.oracle.com/javafx/2/api/javafx/animation/TranslateTransition.html
	 * 
	 * **/
	private void initTestScene() {
		int size_x = 600;
		int size_y = 800;
		
		Pane board = new Pane();
		
		GridPane UI = new GridPane();
		//UI.setGridLinesVisible(true);
		UI.setHgap(0);
		UI.setVgap(0);
		//UI.setAlignment(Pos.CENTER);
		
		Scene game =  new Scene(UI, size_x, size_y);

		
		Polygon ship = new Polygon(new double[]{
										200.0, 0.0, 
										250.0, 50.0, 
										150.0, 50.0});
		
		 
		    TextArea box1 = new TextArea(); // Text boxes for demonstration of UI layout
			TextArea box2 = new TextArea();
			TextArea box3 = new TextArea();
		Label score = new Label("Score 2 : 2");
		
		double squareSize = 20;
		int numTilesSquared = (int)(size_x / squareSize);
		Tile[][] grid = new Tile[numTilesSquared][numTilesSquared];
		for (int i = 0; i < numTilesSquared; i++) {
			for (int j = 0; j < numTilesSquared; j++) {
				grid[i][j] = new Tile(i*squareSize, j*squareSize, squareSize, numTilesSquared);
				grid[i][j].renderTile(board); //this method does not belong inside of TILE
			}
		}
		
		Circle randomCircle = new Circle(50, 50, 50);
		randomCircle.setFill(Color.AQUA);
		ship.setFill(Color.BLACK);
		GridPane.setHalignment(board, HPos.CENTER);
		GridPane.setHalignment(score, HPos.CENTER);
		UI.add(board, 1, 1);
		UI.add(box1, 1, 2);
		UI.add(box2, 0, 1);
		UI.add(box3, 2, 1); 
		UI.add(score, 1, 0);

		
		TranslateTransition ttCircle  = new TranslateTransition(Duration.millis(2000), randomCircle);
		final TranslateTransition ttPolygon = new TranslateTransition(Duration.millis(2000), ship);
		
		//if you click the triangle it will stop, or resume its animation
		ship.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				if(ttPolygon.getStatus() == Status.RUNNING){
					ttPolygon.pause();
				} else {
					ttPolygon.play();
				}
			}
			
		});
		sceneArray.add(game);
		setScreen(sceneArray.size() - 1);

		ttCircle.setByY(200f);
		ttCircle.setCycleCount(2);
		ttCircle.setAutoReverse(true);
		ttPolygon.setByX(200f);
		ttPolygon.setCycleCount(TranslateTransition.INDEFINITE);
		ttPolygon.setAutoReverse(true);
		ttCircle.play();
		ttPolygon.play();	
	}

	private void initTestScene2() {
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
	            //primaryStage.getScene().setWidth(primaryStage.getWidth());
	            //if(this.status == WAITING){
	            //	manager.drawMap();
	            //}
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
	        private void endTurn(){
	        	
	        }
	        private void getNewMap(){
	        	//get the game board from the server
	    		try {
	    			Ship[][] fleet = (Ship[][]) input.readObject();
	    		} catch (ClassNotFoundException e) {
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    		manager.setFleet(fleet);
	        }
	        private void sendNewMap(){
	        	
	        }
	    }.start();
	    System.out.println("Just started the animation timer");
		
	    BorderPane pane = new BorderPane();
		pane.setCenter(canvas);
		pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		/*
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(100);
		column1.setHgrow(Priority.ALWAYS);
		pane.getColumnConstraints().add(0, column1); // each get 50% of width
		*/

		//createAndSetNewScene(pane, size_x, size_y);
		Scene newScene = new Scene(pane, size_x, size_y);
		sceneArray.add(newScene);

		primaryStage.setMinWidth(newScene.getWidth()+15);
		primaryStage.setMinHeight(newScene.getHeight());
	}
	private void createAndSetNewScene(Parent p, int width, int height){
		Scene newScene = new Scene(p, width, height);
		
		sceneArray.add(newScene);

		primaryStage.setWidth((double) width);
		primaryStage.setHeight((double) height);
		primaryStage.setScene(newScene);
		primaryStage.setTitle("Scene " + sceneArray.size());
		
		//primaryStage.sh
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
