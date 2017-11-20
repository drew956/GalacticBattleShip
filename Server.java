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
		private boolean forever = true;
		
		public void run() {
			ServerSocket server;
			try {
				server = new ServerSocket(this.port);
		        while(forever){
		            Socket socket  = server.accept(); //player 1
		            ObjectOutputStream dataOut = new ObjectOutputStream(
		            	socket.getOutputStream()
		            );
		            dataOut.writeUTF("Waiting for player 2");
		            dataOut.flush();
					
		            Socket socket2 = server.accept(); //player 2
		           
		            
		            ObjectOutputStream dataOut2 = new ObjectOutputStream(
		             
	            		socket2.getOutputStream()
		            );
		            dataOut.writeInt(0); //this enables us to transition to the next scene, because we know we have a player 2 if we get our ID
		            dataOut.flush();
		            dataOut2.writeUTF("Welcome player 2!\nBeginning game...");
		            dataOut2.flush();
		            dataOut2.writeInt(1);
		            dataOut2.flush();
		            dataOut.reset();
		            dataOut2.reset();
		            
		            TaskManager task = new TaskManager(dataOut, dataOut2, socket, socket2);
		            //task.setOutputStreams(dataOut, dataOut2);
		            new Thread(task).start();
		        }
		        server.close();
			} catch(Exception e) {
				System.out.println(e);
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