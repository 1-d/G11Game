import greenfoot.*;

/**
 * This is the first World that the user will see when playing the game.
 * 
 * For more detailed comments on the actual gameplay,
 * read the comments in the Game class.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class TitleScreen extends World
{
    private final int STARTING_LEVEL = 0;

    private RectangleText buttonStart;
    private RectangleText buttonHelp;

    private RectangleText instructions;             // store the instruction RectangleText for quick retrieval
    private boolean showInstructions = false;       // whether instructions are currently shown

    /**
     * Constructor for the title screen.
     * Create an interface for the unsuspecting player to use.
     */
    public TitleScreen()
    {
        // create a new world with 930*360 cells
        super(930, 360, 1);

        System.out.println("Hello! Maximize the main Greenfoot screen and ignore this screen.");

        // draw the title
        addObject(new RectangleText("Slippery Stage", 60, 360, 90), 465, 100);

        // create buttons for the player to click
        // begin
        buttonStart = new RectangleText("Begin", 40, 120, 50);
        addObject (buttonStart, 465, 220);
        // instruction
        buttonHelp = new RectangleText("Help", 40, 120, 50);
        addObject (buttonHelp, 465, 300);

        // initialize instructions
        instructions = new RectangleText("INSTRUCTIONS\n\nUse left and right arrows to move,\nbut the floor may be slippery!\nUse the shift key to stop slipping!\n\nUp arrow lets you jump.\nHolding it lets you jump higher.\nDown arrow lets you fall faster.\nCaution: down arrow is tricky to use!\n\nBy: Jason Yuen\nLast update: 2016-01-19", 14, 220, 200);
    }

    /**
     * Check button clicks every single act.
     */
    public void act()
    {
        if (Greenfoot.mousePressed(buttonStart)) {
            // start the game
            Greenfoot.setWorld(new Game(STARTING_LEVEL, 0));
        }

        if (Greenfoot.mousePressed(buttonHelp)) {
            // user clicked a button related to the instructions
            // change the state
            if (showInstructions) removeObject(instructions);
            else addObject(instructions, 160, 200);

            // toggle the boolean
            showInstructions = !showInstructions;
        }
    }
}
