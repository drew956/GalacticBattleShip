import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
	private int type;
	private boolean occupied;
	private int size; // squared size

	public Tile(double x, double y, double squareSize, int gridSize) {
		this.setX(x);
		this.setY(y);
		this.setWidth(squareSize);
		this.setHeight(squareSize);
		this.setFill(null);
		this.setStroke(Color.BLACK);
		size = gridSize;
	}
	
	public void renderTile(Pane board) {
		board.getChildren().add(this);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isOccupied() {
		return false;
	}

	public void setOccupiedStatus(boolean status) {
		occupied = status;
	}

}
