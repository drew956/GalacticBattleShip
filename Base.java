import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Base {

    private int playerID;
    private Coord[] region;
    private transient Image[] sprites;
    private final int SIZE = 4;

    public Base(int playerID) {
        this.playerID = playerID;

        sprites = new Image[2];
        sprites[0] = new Image("file:base_blue.png");
        sprites[1] = new Image("file:base_red.png");
    }

    public boolean inRegion(int x, int y) {
        for (int i = 0; i < SIZE; i++)
            if (x == region[i].getX() && y == region[i].getY())
                return true;

        return false;
    }

    public void drawBase(GraphicsContext gc, double x, double y, double height) {
        Image img = (sprites[playerID]);
        gc.drawImage(img, x, y, height, height);
    }

    /**
     * Inner Class for containing coordinates of Base region on board
     */
    private class Coord {
        private int x, y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
