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
	private final static int FIRST_CLICK  = 0;
	private final static int SECOND_CLICK = 1;
	private final static int WAITING      = 2;
	private int state = 0;//default
	private Stage root;
	private int playerID = 0;
	private Random num = new Random();
	private Image bgImage = new Image("file:bg2.gif");
	private final Vector<Integer> selected = new Vector<Integer>(2,1);
	private int counter = 0;
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
					int tilesHeight = (int) root.getScene().getHeight() / tilesY;
					int indexX = ((int) event.getX()) / tilesHeight;
					int indexY = ((int) event.getY()) / tilesHeight;
					
					if(state == FIRST_CLICK){
						oldX = indexX;
						oldY = indexY;
						selected.set(0, oldX); //storing these so we can highlight the square
						selected.set(1, oldY);
						
						state = SECOND_CLICK;
						System.out.println("First click");
					} else if (state == SECOND_CLICK) {
						newX = indexX;
						newY = indexY;
						//do other processing, such as (if the new square is an enemy attack them,)
						// 							   (but if it is empty, move there)
						if(fleet[oldX][oldY] != null && fleet[oldX][oldY].getPlayerId() == playerID){
							counter++;
							state = FIRST_CLICK; //no matter what we transition back to FIRST_CLICK, and the final
												 //check to see if counter == 2 will set us to WAITING 
							
							if(fleet[newX][newY] == null){
								if(  distanceFromSelected(newX, newY) <= fleet[oldX][oldY].getNumSpaces()){
									fleet[newX][newY] = fleet[oldX][oldY];
									fleet[oldX][oldY] = null;
								}else{
									System.out.println("Unable to move! Too far away!");
								    counter--;
								}
							}else if(fleet[newX][newY].getPlayerId() == fleet[oldX][oldY].getPlayerId() ) { 
								System.out.println("You can't destroy yourself!");
                                counter--;
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
										}
									}else{
										System.out.println("Too far to attack!");
    								    counter--;
									}
								}
							}
						} else {
							System.out.println("Click on one of your ships!");
							state = FIRST_CLICK;
						}
					}
					
					if(counter == 2) { //this is an imperfect implementation
                        state = WAITING;
                        counter = 0;
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
		canvas.setWidth(root.getScene().getHeight());
		canvas.setHeight(root.getScene().getHeight());
		
		
		int height = (int) root.getScene().getHeight() / tilesY;
		for(int i = 0; i < tilesX; i++){
			for(int j = 0; j < tilesY; j++){
				drawRectangle(i * height, j * height, height);
			}
		}
	}

	private void drawRectangle(int x, int y, int height) { //, int width, int height){
		
        gc.setStroke(bg);		
		gc.setFill(this.doneWithTurn() ? Color.GRAY : bg);
		
		//if we already selected a square, and if the square we selected is the current one we are drawing
		//then color the background of the square using transparent color
		if (hasSelectedFirstShip() && hasSelectedSquare(x, y, height)){
			gc.setFill(Color.rgb(255, 8 * 16 + 12, 0, 0.3));
			//#FF8C00 Dark Orange, but looks different due to alpha value
			gc.fillRect(x, y, height, height);

		}
		
		if (hasSelectedFirstShip()) {
			int distance = distanceFromSelected(x / height, y / height);
			if(fleet[selected.get(0)][selected.get(1)] != null){
				if( distance <= fleet[selected.get(0)][selected.get(1)].getNumSpaces() ){
					if( distance != 0 ){
						Color fill = fleet[selected.get(0)][selected.get(1)].getPlayerId() == playerID 
								? Color.rgb(0, 255, 255, 0.3) : Color.rgb(255, 0, 0, 0.3);
								
						gc.setFill(fill);
						//gc.fillText( "" + distance, x + width/2, y + width/2); //gc.getTextBaseline()
						gc.fillRect(x, y, height, height);
					}
				}		
			}
		}
		gc.setStroke(Color.WHITE );
		gc.strokeRect(x, y, height, height);

	}

	private void drawShips(){
		int height = (int) root.getScene().getHeight() / tilesY;
		for(int i = 0; fleet != null && i < fleet.length; i++){
			for(int j = 0; fleet[0] != null && j < fleet[0].length; j++){
				if(fleet[i][j] != null)
					fleet[i][j].drawShip(gc, i * height, j * height, height);
			}
		}
	}

	public Ship[][] getShips() {
		return this.fleet;
	}

	private boolean hasSelectedSquare(int x, int y, int height){
		return (selected.get(0) == x / height && selected.get(1) == y / height) && state == SECOND_CLICK;
	}

	/** 
	 * distanceFromSelected(int currentX, int currentY) expects currentX and currentY to be array indexes,
	 * not pixels. 
	 * so you have to do your x/ width, y / width conversions prior to using this method.
	 * This is to accommodate more use-cases, and for making existing code more readable.
	 */
	private int distanceFromSelected(int currentX, int currentY){
		int selectedX = selected.get(0);
		int selectedY = selected.get(1);
		return ((int) (Math.abs((double)currentX - selectedX)) + (int) (Math.abs((double) currentY - selectedY))); 	
	}
	private boolean hasSelectedFirstShip(){
		return this.state == CanvasManager.SECOND_CLICK;
	}
}
