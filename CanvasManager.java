import java.util.Random;
import java.util.Vector;

import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class CanvasManager {
	private Canvas canvas;
	private GraphicsContext gc;
	private Color bg = Color.BLACK;
	private Ship[][] fleet;
	private int tilesX = 20;
	private int tilesY = 20;
	private int count  = 0;
	private final static int FIRST_CLICK  = 0;
	private final static int SECOND_CLICK = 1;
	private final static int WAITING      = 2;
	private int state = 0;//default
	private Stage root;
	private int playerID = 0;
	private Random num = new Random();
	private Image bgImage = new Image("file:bg2.gif");
	private final Vector<Integer> selected = new Vector<Integer>(2,1);

	/** 
	 * setStage is used to set the stage that will hold this thing.
	 * This allows us to resize the gameBoard dynamically
	 * **/	
	public CanvasManager(Canvas canvas, final Stage rooty){
		this.canvas = canvas;
		gc = canvas.getGraphicsContext2D();
		this.root = rooty;
		initializeShips();
		selected.add(0);
		selected.add(0);
		
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>(){
			
			private int oldX;
			private int oldY;
			private int newX;
			private int newY;
			/* 
			 * Map the X Y coordinates into the slots of the multidimensional array
			 * */
			public void handle(MouseEvent event) {
				if(!doneWithTurn()){
					//determine which tile was clicked
					//and map that to the indexes in the array of ships (basically the map)
					int tileWidth = (int) root.getScene().getWidth() / tilesX;
					int indexX = ((int) event.getX()) / tileWidth;
					int indexY = ((int) event.getY()) / tileWidth;
					
					if(state == FIRST_CLICK){
						oldX = indexX;
						oldY = indexY;
						selected.set(0, oldX); //storing these so we can highlight the square
						selected.set(1, oldY);
						
						state = SECOND_CLICK;
						System.out.println("First click");
					}else if (state == SECOND_CLICK){
						newX = indexX;
						newY = indexY;
						//do other processing, such as (if the new square is an enemy attack them,)
						// 							   (but if it is empty, move there)
						if(fleet[oldX][oldY] != null && fleet[oldX][oldY].getPlayerId() == playerID){
							state = WAITING;

							if(fleet[newX][newY] == null){
								if(  distanceFromSelected(newX, newY) <= fleet[oldX][oldY].getNumSpaces()){
									fleet[newX][newY] = fleet[oldX][oldY];
									fleet[oldX][oldY] = null;
										
								}else{
									System.out.println("Unable to move! Too far away!");
									state = FIRST_CLICK;//so it doesn't just end your turn for making a mistake
								}
							}else if(fleet[newX][newY].getPlayerId() == fleet[oldX][oldY].getPlayerId() ) { 
								state = FIRST_CLICK;
								System.out.println("You can't destroy yourself!");
							} else {
								if(fleet[newX][newY].getPlayerId() != fleet[oldX][oldY].getPlayerId()){
									if( distanceFromSelected(newX, newY) <= fleet[oldX][oldY].getNumSpaces()){
										int luckyNum = num.nextInt(10)+1;
										if (luckyNum <= 8) {
											System.out.println("Ship of Player " + (fleet[newX][newY].getPlayerId() + 1) + " destroyed");
											fleet[newX][newY] = null;
											fleet[newX][newY] = fleet[oldX][oldY];
											fleet[oldX][oldY] = null;
										}
										else {
											System.out.println("Missed! HA HA HA!!");
											//System.out.println("Ship of Player " + (fleet[oldX][oldY].getPlayerId() + 1) + " destroyed");
											//fleet[oldX][oldY] = null;
										}
									}else{
										System.out.println("Too far to attack!");
										state = CanvasManager.FIRST_CLICK;
									}
								}
							}
						} else {
							System.out.println("Click on one of your ships!");
							state = FIRST_CLICK;
						}
					}
				}
			}
			
		});
	}
	public void setPlayerID(int id){
		this.playerID = id;
	}
	public void setFleet(Ship[][] fleet){
		this.fleet = fleet;
	}
	public boolean doneWithTurn(){
		return this.state == CanvasManager.WAITING;
	}
	public void setTurn(){
		this.state = CanvasManager.FIRST_CLICK;
	}
	private void initializeShips(){
		fleet = new Ship[tilesX][tilesY];
		for(int i = 0; i < tilesX; i++){
			for(int j = 0; j < tilesY; j++){
				fleet[i][j] = null;
				//fleet[i][j] = new Ship( j < tilesY / 2 ? 0 : 1);
			}
		}
		//Ship placement
		//we don't increment row because they aren't being placed diagonally
		//they are in a line 
		//also this method never gets called, the server is doing the initialization
		int row = 0;
		for(int i = 0; i < fleet[0].length; i += 3){
			fleet[i][row] = new Ship(0);
			fleet[i][fleet[0].length - 1] = new Ship(1);
			//row++;
		}
	}
	public void drawMap(){
		gc.clearRect(0, 0, root.getScene().getWidth(), root.getScene().getHeight());
		gc.drawImage(bgImage, 0, 0, root.getScene().getWidth(), root.getScene().getHeight());
		drawRectangles();
		drawShips();	
		
	}
	private void drawRectangles(){
		canvas.setWidth(root.getScene().getWidth());
		canvas.setHeight(root.getScene().getWidth());
		
		
		int width = (int) root.getScene().getWidth() / tilesX;
		for(int i = 0; i < tilesX; i++){
			for(int j = 0; j < tilesY; j++){
				drawRectangle(i * width, j * width, width);
			}
		}
	}
	private void drawRectangle(int x, int y, int width) { //, int width, int height){
		
        gc.setStroke(bg);		
		gc.setFill(this.doneWithTurn() ? Color.GRAY : bg);
		
		//if we already selected a square, and if the square we selected is the current one we are drawing
		//then color the background of the square using transparent color
		if(  hasSelectedFirstShip() && hasSelectedSquare(x, y, width)){ 
			gc.setFill(Color.rgb(255, 8 * 16 + 12, 0, 0.3));
			//#FF8C00 Dark Orange, but looks different due to alpha value
			gc.fillRect(x, y, width, width);

		}
		
		if(  hasSelectedFirstShip() ){
			int distance = distanceFromSelected(x / width, y / width);
			if(fleet[selected.get(0)][selected.get(1)] != null){
				if( distance <= fleet[selected.get(0)][selected.get(1)].getNumSpaces() ){
					if( distance != 0 ){
						gc.setFill(Color.rgb(0, 255, 255, 0.3) );
						//gc.fillText( "" + distance, x + width/2, y + width/2); //gc.getTextBaseline()
						gc.fillRect(x, y, width, width);
					}
				}		
			}
		}
		gc.setStroke(Color.WHITE );
		gc.strokeRect(x, y, width, width);

	}
	private void drawShips(){
		int width = (int) root.getScene().getWidth() / tilesX;
		for(int i = 0; fleet != null && i < fleet.length; i++){
			for(int j = 0; fleet[0] != null && j < fleet[0].length; j++){
				if(fleet[i][j] != null)
					fleet[i][j].drawShip(gc, i * width, j * width, width);
			}
		}
	}
	public Ship[][] getShips() {
		return this.fleet;
	}
	private boolean hasSelectedSquare(int x, int y, int width){
		return selected.get(0) == x / width && selected.get(1) == y / width;
	}
	/** 
	 * distanceFromSelected(int currentX, int currentY) expects currentX and currentY to be array indexes,
	 * not pixels. 
	 * so you have to do your x/ width, y / width conversions prior to using this method.
	 * This is to accommodate more use-cases, and for making existing code more readable.
	 * **/
	private int distanceFromSelected(int currentX, int currentY){
		int selectedX = selected.get(0);
		int selectedY = selected.get(1);
		return ((int) (Math.abs((double)currentX - selectedX)) + (int) (Math.abs((double) currentY - selectedY))); 	
	}
	private boolean hasSelectedFirstShip(){
		return this.state == CanvasManager.SECOND_CLICK;
	}
}
