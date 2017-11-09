import java.io.*;
import java.net.*;
import java.lang.Thread;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Server extends Application {
	private GridPane grid;
	private ServerTask taskHandle;
	
    public static void main(String[] args) {
        /*
         * This shouldn't happen until we set the settings
    	ServerTask task = new Server.ServerTask();
        Thread handleConnections = new Thread(task);
        handleConnections.start();
        */
        //gui stuff
        launch(args);
    }

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("GalacticBattleShip Server");
		GridPane pane = new GridPane();
		pane.setHgap(10.0);
		pane.setVgap(10.0);
		pane.setPadding(new Insets(10));
		
		final TextField port = new TextField();
		port.setText("8000");
		final TextArea output = new TextArea();
		Label portLabel = new Label("Enter the port number");
		//portLabel.setTextFill(Color.web("#00FF00"));
		
		final Button start = new Button("Start the server");
		final Button stop  = new Button("Stop the server");

		start.setOnMouseClicked(new EventHandler<MouseEvent>(){

			public void handle(MouseEvent arg0) {
				ServerTask task = new Server.ServerTask();
				taskHandle = task;
				task.setPort(Integer.parseInt(port.getText()));
				//task.setGridPane(grid);
				
				Thread handleConnections = new Thread(task);
		        handleConnections.start();
		        output.appendText("Starting up server on port " + port.getText());

		        start.setDisable(true);
		        port.setDisable(true);
			}
			
			
		});
		stop.setOnMouseClicked(new EventHandler<MouseEvent>(){

			public void handle(MouseEvent arg0) {
				taskHandle.exitThread();
				port.setDisable(false);
				start.setDisable(false);
			}
			
			
		});
		pane.add(portLabel, 0, 0);
		pane.add(port, 1, 0);
		pane.add(start, 1, 1);
		pane.add(stop, 2, 1);
		
		primaryStage.setScene(new Scene(pane, 300, 250) );
		primaryStage.setWidth(600.0);
		primaryStage.setHeight(600.0);
		primaryStage.show();
		
		this.grid = pane;
	}
	public void stop(){
		taskHandle.exitThread();
	}
	public ObservableList<Node> getChildren(){
		return grid.getChildren();
	}
	
	static class ServerTask implements Runnable { 
		private int port = 8000;//default port
		//private GridPane parentGridPane;
		private boolean forever = true;
		
		public void run() {
			ServerSocket server;
			try {
				server = new ServerSocket(this.port);
		        while(forever){
		            Socket socket  = server.accept(); //player 1
		            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		            DataInputStream  in  = new DataInputStream(socket.getInputStream());
		            out.writeUTF("Waiting for player 2\n");      
		            Socket socket2 = server.accept(); //player 2
		            
		            TaskManager task = new TaskManager(in, out, socket2);
		            new Thread(task).start();
		        }
		        server.close();
			} catch(Exception e) {
				System.out.println(e);
				//I was going to re-enable the textfield and 
				//button if the servertask ended,
				//but it shouldn't ever end to be honest.
				//ObservableList<Node> myList = this.parentGridPane.getChildren();
				//myList.forEach();
			}
		}

		public void exitThread() {
			forever = false;
		}

		public void setPort(int parsedInt) {
			this.port = parsedInt;
		}
		/*
		public void setGridPane(GridPane p){
			this.parentGridPane = p;			
		} */
	}

}