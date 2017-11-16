import javafx.scene.paint.Color;
import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;

public class Ship implements Serializable {
	
	private int ID;
	private int numSpaces = 3;
	//private Color color;
	private int playerID;
	private int x;
	private int y;
	private boolean alive = true;
	
	public Ship (int playerID, int x, int y, Color color, int shipID) {
		this.ID = shipID;
		//this.color = color;
		this.playerID = playerID;
		setCoordinates(x,y);
	}
	public Ship (int playerID) {
		//this.color = playerID == 0 ? Color.BLUE : Color.RED;
		this.playerID = playerID;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Color getColor() {
		return playerID == 0 ? Color.BLUE : Color.RED;
    }
	public int getNumSpaces() {
		return numSpaces;
	}
	public int getPlayerId() {
		return playerID;
	}
	public void setShipID(int id) {
		this.ID = id;
	}
	public int getShipID() {
		return ID;
	}
	public void setCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	//public void setColor(Color color) {
	//	this.color = color;
	//}
	public void setNumSpaces(int n) {
		this.numSpaces = n;
	}
	public void setPlayerID(int id) {
		this.ID = ID;
	}
	public void shipDestroyed() {
		alive = false;
	}
	public boolean isAlive() {
		return this.alive;
	}
	public Color getShipColor() {
		return playerID == 0 ? Color.BLUE : Color.RED;
	}
	public void drawShip(GraphicsContext gc, int x, int y, int width) {
		gc.setFill(playerID == 0 ? Color.BLUE : Color.RED);
		gc.setStroke(playerID == 0 ? Color.BLUE : Color.RED);
		if(playerID == 0){
			gc.fillPolygon( new double[] {x + width/6, x + 5 * width / 6,     x + width / 2}, 
							new double[] {y + width/6,      y + width/ 6, y + 5 * width / 6}, 
							3);
		}else {
			gc.fillPolygon( new double[] {    x + width/6,  x + 5 * width / 6, x + width / 2}, 
							new double[] {y + 5 * width/6,   y + 5 * width/ 6, y +  width / 6}, 
							3);
		}    
	}
}
