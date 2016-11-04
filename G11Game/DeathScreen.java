import greenfoot.*;

/**
 * This World is displayed when a level has been cut short
 * or if the player completes the game.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class DeathScreen extends World
{

    private final int LEVEL;
    private final boolean COMPLETED;

    private final int PENALTY_AMOUNT = 50;

    private RectangleText buttonRestart;
    private RectangleText buttonExit;

    /**
     * Constructor for the DeathScreen.
     * Create visual elements for the player.
     * Such as: final score, restart button, exit to title screen.
     */
    public DeathScreen(int level, int score, boolean completed)
    {    
        // Create a new world with 930x400 cells with a cell size of 1x1 pixels.
        super(930, 400, 1);

        // store variables
        LEVEL = level;
        COMPLETED = completed;

        // list the player score
        addObject (new RectangleText("Final Score: " + score, 40, 320, 50), 465, 120);

        if (COMPLETED) {
            // completed description
            buttonRestart = new RectangleText("All levels completed!", 40, 320, 50);
            addObject (buttonRestart, 465, 220);
        }
        else {
            // restart
            buttonRestart = new RectangleText("Restart (Penalty " + (level*PENALTY_AMOUNT) + ")", 40, 320, 50);
            addObject (buttonRestart, 465, 220);
        }
        // exit
        buttonExit = new RectangleText("Menu", 40, 120, 50);
        addObject (buttonExit, 465, 300);
    }

    /**
     * Listen to any button clicks that the player does.
     */
    public void act()
    {
        if (!COMPLETED && Greenfoot.mousePressed(buttonRestart)) {
            // start the game
            Greenfoot.setWorld(new Game(LEVEL, LEVEL*PENALTY_AMOUNT));
        }

        if (Greenfoot.mousePressed(buttonExit)) {
            // go back to the menu
            Greenfoot.setWorld(new TitleScreen());
        }
    }
}
