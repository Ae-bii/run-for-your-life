import java.util.Arrays;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;

/**
 * This class creates a dinosaur which is the player.
 * 
 * @author Anupam Bhakta
 * @author Dylan Lian
 */
public class Player{
    private double centerY;
    private double velocityY;
    private Image dinosaur;
    private Image[] animatedDinosaur = {new Image(50, 200, "dinosaur1.png"), new Image(50, 200, "dinosaur2.png")};
    private boolean leftLeg = false;

    /**
     * Creates a new dinosaur sprite for the player to control
     */
    public Player() {
        dinosaur = animatedDinosaur[0];
        centerY = dinosaur.getCenter().getY();
        velocityY = 8;
    }

    /**
     * Changes the sprite between two images to animate the player
     * @param canvas the CanvasWindow the game is running on
     */
    public void setSprite(CanvasWindow canvas) {
        if (leftLeg) {
            dinosaur.setImagePath("dinosaur2.png");
        } else {
            dinosaur.setImagePath("dinosaur1.png");
        }
        leftLeg = !leftLeg;
     }

    /**
     * Moves the player sprite up
     * @param canvas the CanvasWindow the game is running on
     */
     public void goUp(CanvasWindow canvas) {
        if (centerY - (dinosaur.getHeight()/2) >= 5) {
            centerY -= velocityY;
            dinosaur.moveBy(0, -velocityY);
        }
    }

    /**
     * Moves the player sprite down
     * @param canvas the CanvasWindow the game is running on
     */
    public void goDown(CanvasWindow canvas) {
        if (centerY + (dinosaur.getHeight()/2) <= GameManager.CANVAS_HEIGHT-7) {
            centerY += velocityY;
            dinosaur.moveBy(0, velocityY);
        }
    }

    public double getWidth() {
        return dinosaur.getWidth();
    }

    public double getHeight() {
        return dinosaur.getHeight();
    }

    public double getX() {
        return dinosaur.getX();
    }

    public double getY() {
        return dinosaur.getY();
    }
    
    public void add(CanvasWindow canvas) {
        canvas.add(dinosaur);
    }

    public void remove(CanvasWindow canvas) {
        canvas.remove(dinosaur);
    }

    @Override
    public String toString() {
        return "Player [animatedDinosaur=" + Arrays.toString(animatedDinosaur) + ", centerY=" + centerY + ", dinosaur="
            + dinosaur + ", leftLeg=" + leftLeg + ", velocityY=" + velocityY + "]";
    }
}
