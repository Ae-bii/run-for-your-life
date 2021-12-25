import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.TextAlignment;
import edu.macalester.graphics.events.Key;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class governs all the game mechanics and ties everything together.
 * 
 * @author Anupam Bhakta
 * @author Dylan Lian
 */
public class GameManager {
    public static final int CANVAS_WIDTH = 1100;
    public static final int CANVAS_HEIGHT = 400;
    public static final Color gray = new Color(247, 247, 247);

    private CanvasWindow canvas;
    private Player player;
    private List<Cactus> cacti;
    private List<Lives> hearts;
    private Stack<Lives> lives;
    private Background ground;
    private Timer animateMove;
    private Timer diffTimer;
    private Timer scoreTimer;
    private Timer spawnHeart;
    private int difficulty;
    private Random random;
    private Set<Key> keys;
    private int score;
    private int finalScore;
    private int highScore;
    private GraphicsText scoreText;
    private GraphicsText finalS;
    private GraphicsText highScoreText;

    private Image title;
    private Image play;
    private Image gameOverText;
    private Image playAgainText;

    private int count;
    
    /**
     * Creates the canvas window as well as all the graphics objects related to it.
     * Initializes the main menu screen
     */
    public GameManager() {

        canvas = new CanvasWindow("Run for your life", CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setBackground(gray);

        random = new Random();
        title = new Image(0,0, "title.png");
        title.setCenter(canvas.getCenter().getX(),canvas.getCenter().getY()-100);
        title.setScale(0.5);
        play = new Image(0,0, "play.png");
        play.setCenter(canvas.getCenter());
        play.setScale(0.5);
        gameOverText = new Image(0,0, "gameover.png");
        gameOverText.setCenter(canvas.getCenter().getX(), canvas.getCenter().getY() - 60);
        gameOverText.setScale(0.7);
        playAgainText = new Image(0, 0, "playagain.png");
        playAgainText.setCenter(canvas.getCenter());
        playAgainText.setScale(0.5);
        
        ground = new Background();

        initialize();

        animateMove = new Timer();
        diffTimer = new Timer();
        spawnHeart = new Timer();
        scoreTimer = new Timer();

        menuScreen();
    }

    /**
     * Initializes many of the game objects which also setting counters to their respective values.
     */
    private void initialize() {
        difficulty = 1;
        score = 0;
        count = 1;

        player = new Player();

        cacti = new ArrayList<>();
        hearts = new ArrayList<>();
        
        lives = new Stack<>();
        lives.push(new Lives(CANVAS_WIDTH-5, 19));
        lives.peek().setSpriteToNormal();
        lives.push(new Lives(lives.peek().getX()+32, 19));
        lives.peek().setSpriteToNormal();
        lives.push(new Lives(lives.peek().getX()+32, 19));
        lives.peek().setSpriteToNormal();

        scoreText = new GraphicsText("Score: " + score);
        scoreText.setFont("ElfBoyClassic", FontStyle.PLAIN, 40);
        scoreText.setAlignment(TextAlignment.CENTER);
        scoreText.setCenter(canvas.getCenter().getX(), 11);
    }

    /**
     * Runs the actual game by adding the objects to the canvas.
     */
    private void run() {   
        ground.add(canvas);
        // Fix array out of bounds exception
        lives.get(0).add(canvas);
        lives.get(1).add(canvas);
        lives.get(2).add(canvas);
        canvas.add(scoreText);     
        player.add(canvas);

        /**
         * Animates the player's feet movement
         */
        animateMove.schedule(new TimerTask() {
            @Override
            public void run() {
               player.setSprite(canvas);
            }
        }, 0, 100);

        /**
         * Raises the difficulty every 15 seconds
         */
        diffTimer.schedule(new TimerTask() {
            @Override
            public void run() {
               difficulty++;
            }
        }, 0, 15000);

        /**
         * Updates the score at the top center of the screen
         */
        scoreTimer.schedule(new TimerTask() {
            @Override
            public void run() {
               score++;
               scoreText.setText("Score: " + score);
            }
        }, 0, (100-5*difficulty));

        /**
         * Spawns hearts randomly on the screen every 30 seconds
         */
        spawnHeart.schedule(new TimerTask() {
            @Override
            public void run() {
               spawnLife();
            }
        }, 0, 30000);

        /**
         * Creates the illusion of movement by moving the ground and all the other graphics objects
         */
        canvas.animate(() -> {
            ground.move(difficulty);

            if (lives.size() > 0) {
                keys = canvas.getKeysPressed();

                if (keys.contains(Key.W) || keys.contains(Key.UP_ARROW)) {
                    player.goUp(canvas);
                }
        
                if (keys.contains(Key.S) || keys.contains(Key.DOWN_ARROW)) {
                    player.goDown(canvas);
                }
            }

            if (lives.size() <= 0 && count == 1) {
                player.remove(canvas);

                finalScore = score;
                if (finalScore >= highScore) {
                    highScore = finalScore;
                }
                canvas.remove(scoreText);

                highScoreText = new GraphicsText("High Score: " + highScore);
                highScoreText.setFont("ElfBoyClassic", FontStyle.PLAIN, 38);
                highScoreText.setAlignment(TextAlignment.LEFT);
                highScoreText.setCenter(120, 9);
                canvas.add(highScoreText);
                
                // Make highscore permanent
                finalS = new GraphicsText("Score: " + finalScore);
                finalS.setFont("ElfBoyClassic", FontStyle.PLAIN, 40);
                finalS.setAlignment(TextAlignment.CENTER);
                finalS.setCenter(canvas.getCenter().getX(), 10);
                canvas.add(finalS);

                gameOver();
                count--;
            }

            spawnCactus();
            
            List<Lives> heartsClone = new ArrayList(hearts);
            for (Lives heart : heartsClone) {
                heart.move(difficulty);
                if (heart.checkCollision(player)) {
                    heart.remove(canvas);
                    hearts.remove(heart);
                    if (lives.size() < 5) {
                        lives.push(new Lives(lives.peek().getX()+32, 19));
                        lives.peek().setSpriteToNormal();
                        lives.peek().add(canvas);
                    }
                }
                if (heart.isOutOfScreen(canvas)) {
                    hearts.remove(heart);
                    heart.remove(canvas);
                }
            }
            cactusCollision();
            canvas.draw();
        });
    }

    /**
     * Creates the cacti on the screen and also checks if it leaves the screen.
     */
    private void cactusCollision() {
        List<Cactus> clone = new ArrayList(cacti);
        for (Cactus cactus : clone) {
            cactus.move(difficulty);
            if (cactus.checkCollision(player)) {
                if(lives.size() > 0) {
                    cacti.remove(cactus);
                    cactus.remove(canvas);
                    lives.peek().remove(canvas);
                    lives.pop();
                }
            }
            if(cactus.isOutOfScreen(canvas)) {
                cacti.remove(cactus);
                cactus.remove(canvas);
            }
        }
    }

    /**
     * Creates the menu screen from which the player can click play.
     */
    private void menuScreen() {
        canvas.add(title);
        canvas.add(play);
        canvas.draw();

        canvas.onMouseMove(event -> {
            if (event.getPosition().getX() >= getPlayLeftBound() && event.getPosition().getX() <= getPlayRightBound() &&  event.getPosition().getY() >= getPlayTopBound() && event.getPosition().getY() <= getPlayBottomBound()) {
                play.setScale(0.6);
                play.setImagePath("hoverplay.png");
            } else {
                play.setScale(0.5);
                play.setImagePath("play.png");
            }
        });

        canvas.onMouseDown(event -> {
            if (event.getPosition().getX() >= getPlayLeftBound() && event.getPosition().getX() <= getPlayRightBound() &&  event.getPosition().getY() >= getPlayTopBound() && event.getPosition().getY() <= getPlayBottomBound()) {
                canvas.removeAll();
                run();
            }
        });

    }

    /**
     * Creates the game over screen when the player runs out of lives.
     */
    private void gameOver() {
        canvas.add(gameOverText);
        canvas.add(playAgainText);
        canvas.draw();

        canvas.onMouseMove(event -> {
            if (event.getPosition().getX() >= getReplayLeftBound() && event.getPosition().getX() <= getReplayRightBound() &&  event.getPosition().getY() >= getReplayTopBound() && event.getPosition().getY() <= getReplayBottomBound()) {
                playAgainText.setScale(0.6);
                playAgainText.setImagePath("hoverplayagain.png");
            } else {
                playAgainText.setScale(0.5);
                playAgainText.setImagePath("playagain.png");
            }
        });

        canvas.onMouseDown(event -> {
            if (event.getPosition().getX() >= getPlayLeftBound() && event.getPosition().getX() <= getPlayRightBound() &&  event.getPosition().getY() >= getPlayTopBound() && event.getPosition().getY() <= getPlayBottomBound()) {
                try {
                    replay();
                } catch (Exception e) {}
            }
        });
    }
    
    /**
     * Allows for the game to be played again if try again is clicked
     */
    private void replay() {
        canvas.removeAll();
        initialize();
        ground.add(canvas);
        lives.get(0).add(canvas);
        lives.get(1).add(canvas);
        lives.get(2).add(canvas);
        canvas.add(scoreText);     
        player.add(canvas);
    }

    private double getPlayLeftBound() {
        return play.getCenter().getX() - 40;
    }

    private double getPlayRightBound() {
        return play.getCenter().getX() + 40;
    }

    private double getPlayTopBound() {
        return play.getCenter().getY() - 10;
    }

    private double getPlayBottomBound() {
        return play.getCenter().getY() + 10;
    }
    
    // Gets bounds for Play Again test
    private double getReplayLeftBound() {
        return playAgainText.getCenter().getX() - 120;
    }

    private double getReplayRightBound() {
        return playAgainText.getCenter().getX() + 120;
    }

    private double getReplayTopBound() {
        return playAgainText.getCenter().getY() - 10;
    }

    private double getReplayBottomBound() {
        return playAgainText.getCenter().getY() + 10;
    }

    private void spawnLife() {
        Lives lifeItem = new Lives(1200, random.nextInt(400));
        lifeItem.add(canvas);
        hearts.add(lifeItem);
    }
    
    private void spawnCactus() {
        if (random.nextInt(100) < 10+difficulty) {
            int numCactus = random.nextInt(2);
            for (int i = 0; i < numCactus; i++) {
                Cactus cactus = new Cactus(1200, random.nextInt(400));
                cacti.add(cactus);
                cactus.add(canvas);
            }
        }
    }

    @Override
    public String toString() {
        return "GameManager [animateMove=" + animateMove + ", cacti=" + cacti + ", canvas=" + canvas + ", color="
            + gray + ", count=" + count + ", diffTimer=" + diffTimer + ", difficulty=" + difficulty + ", finalS="
            + finalS + ", gameOverText=" + gameOverText + ", ground=" + ground + ", hearts=" + hearts + ", keys=" + keys
            + ", lives=" + lives + ", play=" + play + ", playAgainText=" + playAgainText + ", player=" + player
            + ", random=" + random + ", score=" + score + ", scoreText=" + scoreText + ", scoreTimer=" + scoreTimer
            + ", spawnHeart=" + spawnHeart + ", title=" + title + "]";
    }

    public static void main(String[] args) {
        new GameManager();
    }
}
