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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.animation.Animation.Status;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

/* Tutorial on game development using javafx, useful for creating the game loop
 * https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
 * 
 * */
public class Client extends Application {
	BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
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
		initGameScene(); //putting each scene into its own function will allow us to tweak them easier
		initTestScene();
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
		
		//not using because it doesn't overlay where you'd expect
		/*Polygon ship = new Polygon(new double[]{
										200.0, 0.0, 
										250.0, 50.0, 
										150.0, 50.0});
		*/
		
		//ship.setFill(Color.BLACK);
		GridPane pane = new GridPane();
		pane.add(canvas, 0, 0);
		//pane.getChildren().add(ship);
		
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
	 * initTestScene is currently being used to test if using a normal
	 * GridPane or other pane would be more effective than using a canvas <br />
	 * Upon further testing I have concluded that this will be far easier to use than the Canvas element
	 * 
	 * TranslateTransitions are dope:
	 * https://docs.oracle.com/javafx/2/api/javafx/animation/TranslateTransition.html
	 * 
	 * **/
	private void initTestScene() {
		Scene gameBoard;
		Pane board = new Pane();
		gameBoard =  new Scene(board, 600, 600);
		Polygon ship = new Polygon(new double[]{
										200.0, 0.0, 
										250.0, 50.0, 
										150.0, 50.0});

		Circle randomCircle = new Circle(50, 50, 50);
		randomCircle.setFill(Color.AQUA);
		ship.setFill(Color.BLACK);
		board.getChildren().addAll(ship, randomCircle);
		
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
		sceneArray.add(gameBoard);
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
		} else {
			System.out.println("setScreen(int i): You've entered an invalid index for your scene.\n");
		}
	}

}
