import greenfoot.*;

/**
 * This class requires a World to function.
 * The info bar is designed for the class Game (which inherits from World).
 * 
 * This class provides a convenient way of manipulating the info bar at the top of the screen.
 * RectangleText will be shown so that the user knows about their current state.
 * The possible manipulations are listed below:
 * 1. Score and lives can be updated.
 * 2. Check whether the player pressed the exit button.
 * 
 * Because this object is not added to a World, act() will not run every frame.
 * So to compensate, the actual Game will check this object every frame.
 * 
 * @author Jason Yuen 
 * @version v105
 */
public class InfoBar
{

    private final int HEIGHT;
    private final int BUTTON_WIDTH = 100;
    private World game;
    private RectangleText buttonExit;
    private RectangleText gameInfoText;

    // info that will be displayed
    private final String INFO_FORMAT = "Score: %06d  Lives: %d";
    private int score;
    private int lives;

    /**
     * Constructor for the InfoBar.
     * Gets basic information about the player then updates once.
     */
    public InfoBar(World currentGame, int barHeight)
    {
        game = currentGame;
        HEIGHT = barHeight;

        // create buttons for the player to click
        buttonExit = new RectangleText("Quit Early", 24, BUTTON_WIDTH, HEIGHT);
        game.addObject (buttonExit, game.getWidth() - BUTTON_WIDTH/2, HEIGHT/2);

        // update the actual info bar
        updateInfo(0, 3);
    }

    /**
     * This method checks whether the player is pressing on the exit button.
     */
    public boolean pressedExit() 
    {
        return Greenfoot.mousePressed(buttonExit);
    }

    /**
     * Replaces the known information with the new information.
     * Then updates the information seen on the screen.
     */
    public void updateInfo(int newScore, int newLives) 
    {
        // replace info
        score = newScore;
        lives = newLives;
        
        // update
        update();
    }

    /**
     * Updates the gameInfoText.
     * This will update what is seen on the screen.
     */
    private void update()
    {
        // clear this rectangle text if it still exists
        if (gameInfoText!=null) {
            game.removeObject(gameInfoText);
        }

        // display a new rectangle
        // this string to be displayed is: String.format(INFO_FORMAT, score)
        gameInfoText = new RectangleText(String.format(INFO_FORMAT, score, lives), 24, game.getWidth()-BUTTON_WIDTH, HEIGHT);
        game.addObject(gameInfoText, (game.getWidth()-BUTTON_WIDTH)/2, HEIGHT/2);
    }
}
