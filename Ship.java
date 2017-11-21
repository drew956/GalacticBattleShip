import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ship implements Serializable {
	
	private int ID;
	private int numSpaces = 3;
	//private Color color;
	private int playerID;
	private int x;
	private int y;
	private boolean alive = true;
	private transient Image[] sprites;
	
	public Ship (int playerID, int x, int y, Color color, int shipID) {
		this.ID = shipID;
		//this.color = color;
		this.playerID = playerID;
		setCoordinates(x,y);
		sprites = new Image[2];
		sprites[0] = new Image("file:p1.png", true);
		sprites[1] = new Image("file:p2.png", true);
	}
	public Ship (int playerID) {
		//this.color = playerID == 0 ? Color.BLUE : Color.RED;
		this.playerID = playerID;
		sprites = new Image[2];
		sprites[0] = new Image("file:p1.png", true);
		sprites[1] = new Image("file:p2.png", true);
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
		Image img = (playerID == 0 ? sprites[0] : sprites[1]);
		gc.drawImage(img, x, y, width, width);
	}
	public void drawShipOld(GraphicsContext gc, int x, int y, int width) {
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
	private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    // custom deserialization:
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.sprites = new Image[2];
        this.sprites[0] = new Image("file:p1.png");
        this.sprites[1] = new Image("file:p2.png");
    }
}
