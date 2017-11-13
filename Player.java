import java.awt.Color;
import java.awt.color.ColorSpace;

public class Player {
	
	private Ship ships[] = new Ship[6];
	private Color color;
	private int moneys; 
	private String userName;
	private int playerID;
	private String shipColor;
	
	public Player(String userName, int playerID) {
		this.userName = userName;
		this.playerID = playerID;
	}
	public void setPLayerID(int playerID) {
		this.playerID = playerID; 
	}
	public int getPLayerID() {
		return playerID; 
	}
	public void setUserName(String userNames) {
		this.userName = userNames; 
	}
	public String getUserName() {
		return userName; 
	}
	public int getShipCount() {
		return ships.length; 
	}
	/*
	 * Sends move to Ship class
	 */
	public void setMove(int x, int y, int shipID) {
		for (int i = 0; i < ships.length; i++) {
			if (ships[i].getShipID() == shipID) {
				ships[i].setCoordinates(x, y);
			}
		}
	}
	public String getColor() {
		return shipColor;
	}
	public void initializeShips(int plaerID, int x, int y, String shipColor, int shipID) {
		//Sets the ship color either blue or red
		if (shipColor == "blue") { 
			this.shipColor = shipColor;
			color = new Color(0,0,255); //RGB value
		}
		else {
			this.shipColor = shipColor;
			color = new Color(255,0,0);

		}
		//Initializes new ship for each spot in the array
		for (int i = 0; i < ships.length; i++) {
			ships[i] = new Ship(playerID, x, y, color, shipID);
			shipID++;
		}
	}
}
