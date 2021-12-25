import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;

/**
 * This class creates a single cactus which is used by the GameManager class to create multiple
 * cactus obstacles.
 * 
 * @author Anupam Bhakta
 * @author Dylan Lian
 */
public class Cactus extends Moveable {
    private Image cactus;

    /**
     * Creates a single cactus
     * 
     * @param x Horizontal position of the heart
     * @param y Vertical position of the heart
     */
    public Cactus(int x, int y) {
        cactus = new Image(0, 0, "cactus1.png");
        cactus.setCenter(x, y);
    }

    public void move(int difficulty) {
        cactus.setCenter(cactus.getCenter().getX() - (5 + difficulty), cactus.getCenter().getY());

    }

    public boolean isOutOfScreen(CanvasWindow canvas) {
        if (cactus.getCenter().getX() < -10) {
            return true;
        }
        return false;
    }

    public boolean checkCollision(Player dinosaur) {
        if (checkLeft(dinosaur) && checkRight(dinosaur)) {
            if (checkBottom(dinosaur) && checkTop(dinosaur)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLeft(Player dinosaur) {
        if (dinosaur.getX() + dinosaur.getWidth() / 2 >= cactus.getX() - cactus.getWidth() / 2) {
            return true;
        }
        return false;
    }

    private boolean checkRight(Player dinosaur) {
        if (dinosaur.getX() - dinosaur.getWidth() / 2 <= cactus.getX() + cactus.getWidth() / 2 - 10) {
            return true;
        }
        return false;
    }

    private boolean checkBottom(Player dinosaur) {
        if (dinosaur.getY() - dinosaur.getHeight() / 2 <= cactus.getY() + cactus.getHeight() / 2 - 2) {
            return true;
        }
        return false;
    }

    private boolean checkTop(Player dinosaur) {
        if (dinosaur.getY() + dinosaur.getHeight() / 2 >= cactus.getY() - cactus.getHeight() / 2 + 7) {
            return true;
        }
        return false;
    }

    public void add(CanvasWindow canvas) {
        canvas.add(cactus);
    }

    public void remove(CanvasWindow canvas) {
        canvas.remove(cactus);
    }

    @Override
    public String toString() {
        return "Cactus [cactus=" + cactus + "]";
    }
}
