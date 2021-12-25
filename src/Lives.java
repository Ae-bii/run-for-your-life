import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;

/**
 * This class creates a the lives at the top left of the screen but also the lives that spawn on the
 * map.
 * 
 * @author Anupam Bhakta
 * @author Dylan Lian
 */
public class Lives extends Moveable {
    private Image heart;

    /**
     * Creates a single heart
     * 
     * @param x horizontal position of the heart
     * @param y vertical position of the heart
     */
    public Lives(double x, double y) {
        heart = new Image(0, 0, "shinebubbleheart2.png");
        heart.setScale(0.1);
        heart.setCenter(x, y);
    }

    public void move(int difficulty) {
        heart.setCenter(heart.getCenter().getX() - (5 + difficulty), heart.getCenter().getY());
    }

    public boolean isOutOfScreen(CanvasWindow canvas) {
        if (heart.getCenter().getX() < -10) {
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

    public void setSpriteToNormal() {
        heart.setImagePath("heart.png");
    }

    private boolean checkLeft(Player dinosaur) {
        if (dinosaur.getX() + dinosaur.getWidth() / 2 >= heart.getX() - heart.getWidth() / 2 + 280) {
            return true;
        }
        return false;
    }

    private boolean checkRight(Player dinosaur) {
        if (dinosaur.getX() - dinosaur.getWidth() / 2 <= heart.getX() + heart.getWidth() / 2 - 4) {
            return true;
        }
        return false;
    }

    private boolean checkBottom(Player dinosaur) {
        if (dinosaur.getY() - dinosaur.getHeight() / 2 <= heart.getCenter().getY() + heart.getImageHeight() / 2 - 170) {
            return true;
        }
        return false;
    }

    private boolean checkTop(Player dinosaur) {
        if (dinosaur.getY() + dinosaur.getHeight() / 2 >= heart.getCenter().getY() - heart.getImageHeight() / 2 + 125) {
            return true;
        }
        return false;
    }

    public double getX() {
        return heart.getCenter().getX();
    }

    public void add(CanvasWindow canvas) {
        canvas.add(heart);
    }

    public void remove(CanvasWindow canvas) {
        canvas.remove(heart);
    }

    @Override
    public String toString() {
        return "Lives [heart=" + heart + "]";
    }    
}
