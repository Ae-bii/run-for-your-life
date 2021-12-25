import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;

/**
 * This class creates a background which cycles between three sprites in order to have a fluid, continious motion effect.
 * 
 * @author Anupam Bhakta
 * @author Dylan Lian
 */
public class Background {
    private Image ground1;
    private Image ground2;
    private Image ground3;

    /**
     * Creates a new background that initializes three images that scroll continuously
     */
    public Background() {
        ground1 = new Image(0, 0, "graybackground.png");
        ground2 = new Image(0, 0, "graybackground.png");
        ground3 = new Image(0, 0, "graybackground.png");

        ground1.setScale(0.8);
        ground2.setScale(0.8);
        ground3.setScale(0.8);
    }

    /**
     * Adds the ground images to the canvas
     * @param canvas the CanvasWindow that the gaem is running on
     */
    public void add(CanvasWindow canvas) {
        ground1.setCenter(canvas.getCenter());
        ground2.setCenter(ground1.getCenter().getX() + ground1.getImageWidth()/2, canvas.getCenter().getY());
        ground3.setCenter(ground2.getCenter().getX() + ground2.getImageWidth()/2, canvas.getCenter().getY());
        canvas.add(ground1);
        canvas.add(ground2);
        canvas.add(ground3);
    }

    /**
     * Moves the backgrounds and cycles the three identical images so that when one gets off the screen it moves back to the beginning
     */
    public void move(int difficulty) {
        if (isOutOfScreen(ground1)) {
            ground1.setCenter(ground3.getCenter().getX() + ground3.getImageWidth()/2, ground1.getCenter().getY());
        }
        if (isOutOfScreen(ground2)) {
            ground2.setCenter(ground1.getCenter().getX() + ground1.getImageWidth()/2, ground2.getCenter().getY());
        }
        if (isOutOfScreen(ground3)) {
            ground3.setCenter(ground2.getCenter().getX() + ground2.getImageWidth()/2, ground3.getCenter().getY());
        }

        ground1.setCenter(ground1.getCenter().getX()-(5+difficulty), ground1.getCenter().getY());
        ground2.setCenter(ground2.getCenter().getX()-(5+difficulty), ground2.getCenter().getY());
        ground3.setCenter(ground3.getCenter().getX()-(5+difficulty), ground3.getCenter().getY());
    }

    private boolean isOutOfScreen(Image ground) {
        if (ground.getCenter().getX() + ground.getImageWidth()/2 < -5) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Background [ground1=" + ground1 + ", ground2=" + ground2 + ", ground3=" + ground3 + "]";
    }
}
