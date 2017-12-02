import javafx.scene.image.Image;

public class Base {

    private int ID;
    private Coord[] region;
    private transient Image sprite;
    private final int SIZE = 4;

    public Base(int playerID, int initX, int initY) {
        this.ID = playerID;

        if (this.ID == 0) {
            initBlue(initX, initY);
        } else {
            initRed(initX, initY);
        }
    }

    public boolean inRegion(int x, int y) {
        for (int i = 0; i < SIZE; i++)
            if (x == region[i].getX() && y == region[i].getY())
                return true;

        return false;
    }

    private void initBlue(int initX, int initY) {
        sprite = new Image("file:baseb.png", true);

        region[0].setX(initX);
        region[0].setY(initY);

        region[1].setX(initX+1);
        region[1].setY(initY);

        region[2].setX(initX);
        region[2].setY(initY+1);

        region[3].setX(initX+1);
        region[3].setY(initY+1);
    }

    private void initRed(int initX, int initY) {
        sprite = new Image("file:baser.png", true);

        region[0].setX(initX);
        region[0].setY(initY);

        region[1].setX(initX-1);
        region[1].setY(initY);

        region[2].setX(initX);
        region[2].setY(initY-1);

        region[3].setX(initX-1);
        region[3].setY(initY-1);
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
