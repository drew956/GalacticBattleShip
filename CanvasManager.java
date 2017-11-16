import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
	

	/** 
	 * setStage is used to set the stage that will hold this thing.
	 * This allows us to resize the gameBoard dynamically
	 * **/	
	public CanvasManager(Canvas canvas, final Stage rooty){
		this.canvas = canvas;
		gc = canvas.getGraphicsContext2D();
		this.root = rooty;
		initializeShips();
		
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
								if( ((Math.abs((double)newX - oldX)) + (Math.abs((double) newY - oldY)) ) <= fleet[oldX][oldY].getNumSpaces()){
									fleet[newX][newY] = fleet[oldX][oldY];
									fleet[oldX][oldY] = null;
										
								}else{
									System.out.println("Unable to move! Too far away!");
									state = FIRST_CLICK;//so it doesn't just end your turn for making a mistake
								}
							}else if(fleet[newX][newY] == fleet[oldX][oldY] ) { 
								state = FIRST_CLICK;
								System.out.println("You can't destroy yourself!");
							} else {
								if(fleet[newX][newY].getPlayerId() != fleet[oldX][oldY].getPlayerId()){
									System.out.println(
										fleet[newX][newY].getPlayerId() == 0 ? "Ship of Player 1 destroyed" : "Ship of Player 2 destroyed"
									);
									fleet[newX][newY] = null;
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
		
		for(int i = 0; i < fleet[0].length; i += 3){
			fleet[i][0] = new Ship(0);
			fleet[i][fleet[0].length - 1] = new Ship(1);
		}
	}
	public void drawMap(){
		//count++;
		//System.out.println("Called drawMap " + count + "times");
		gc.clearRect(0, 0, root.getScene().getWidth(), root.getScene().getHeight());
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
		//int width = (int) canvas.getWidth() / tilesX;
		
		//gc.clearRect(x, y, width, width);
        gc.setStroke(bg);		
		gc.setFill(this.doneWithTurn() ? Color.GRAY : bg);
		gc.fillRect(x, y, width, width);
		
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
}
