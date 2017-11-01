public class Client {

	private boolean myTurn;

	private DataInputStream input;

	private DataOutputStream output;

	private boolean baseStatus;

	private Ship[][] fleet;

	private Tile[][] map;

	private Player player;

	public boolean selectShip(int x, int y) {
		return false;
	}

	public boolean moveShip(Ship boat, int x1, int y1) {
		return false;
	}

	public boolean attackShip(Ship attackingBoat, int x, int y) {
		return false;
	}

	public void sendToServer() {

	}

	public void getFromServer() {

	}

	public void checkBaseStatus() {

	}

	public void connectToServer() {

	}

	public void run() {

	}

	public void waitForOwnTurn() {

	}

}
