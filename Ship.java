import java.awt.Color;
import java.awt.color.ColorSpace;

public class Ship {
	
	private int ID;
	private int numSpaces;
	private Color color;
	private int playerID;
	private int x;
	private int y;
	private boolean alive = true;
	
	public Ship (int playerID, int x, int y, Color color, int shipID) {
		this.ID = shipID;
		this.color = color;
		this.playerID = playerID;
		setCoordinates(x,y);
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}
	public int getNumSpaces() {
		return numSpaces;
	}
	public int getPlayerId() {
		return ID;
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
	public void setColor(Color color) {
		this.color = color;
	}
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
	public ColorSpace getShipColor() {
		return color.getColorSpace();
	}
}
