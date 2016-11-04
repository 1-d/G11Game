import greenfoot.*;
import java.util.Scanner;
import java.io.File;
import java.util.HashSet;

/**
 * This world will let the player play a single level.
 * Max score is 3306. Are you ready?
 * 
 * 
 * 
 * Gameplay:
 * This is a game where you have to survive as long as possible.
 * 
 * The game scrolls slowly to the right, which the player has no control over.
 * The player starts with exactly 3 lives at the beginning of each level.
 * An enemy results in one life lost if the player hits it (except exploders can instantly kill, those are scary).
 * 
 * You survive by avoiding enemies and staying on the screen.
 * Arrow keys + shift key are used to control the player.
 * Details on the user input are in the "How To Play" section.
 * 
 * The player's score is determined by the number of columns passed and the number of levels completed.
 * Restarting a level will mean a penalty to the score (except tutorial).
 * 
 * 
 * 
 * Levels:
 * There are three levels in the game and a tutorial stage.
 * 
 * The tutorial level is very straightforward and easily survivable if the user has common sense.
 * 
 * Level 1 requires some knowledge of how the game works.
 * This level tests if you have basic skills to play the game.
 * This level also introduces the slippery player mechanic, so watch out!
 * 
 * Level 2 is quite a large difficulty jump from level 1.
 * This level is built to test your skills and patience.
 * The slippery player mechanic is strong in this level!
 * By finishing this level, you can gain a confidence boost.
 * 
 * Level 3 is 20% done.
 * This level is meant to challenge the creator of the game.
 * However, it is currently built like a bonus level.
 * Have fun trying to get to the bonus stage!
 * 
 * 
 * 
 * How To Play:
 * The game only accepts mouse clicks (for buttons) and the 4 arrow keys + shift (for the player)
 * The mouse can click on any button, especially the labelled buttons with commands.
 * 
 * The left and right arrows move the player left and right.
 * Each act, the movement speed slows down a little, giving the feeling of a slippery player.
 * 
 * The shift key will stop the slipping.
 * It also results in slowed movement, and doesn't affect jumps.
 * 
 * The up arrow lets the player jump.
 * If the player is in the air, the up arrow lets the player fall down slower.
 * Holding the up arrow for a long time can let you jump up to 4 blocks.
 * 
 * The down arrow only works when the player is in the air.
 * The down arrow forces the player to fall down faster.
 * I recommend not to use the down arrow often.
 * 
 * 
 * 
 * Known bugs/issues (sorted by importance):
 * Issue.  The world size changes from the title screen to the game screen.
 *         This results in a jerky transition if Greenfoot is not maximized.
 * Bug.    The player can jump high enough and move up and away from the main level (but must fall down since no blocks are there).
 *         The player will appear behind the InfoBar, making this strategy fairly unreliable.
 *         Can technically be considered a feature instead of a bug.
 *         I will let this slide because it makes the beginning of level 2 easier.
 * Issue.  The player must manually ignore the output screen.
 * Issue.  The exploder is not used often enough in the level design.
 * Issue.  There are not enough levels. As a result, level difficulty suddenly increases.
 * Issue.  No instructions are given on how to make a level.
 * Issue.  Level 3 is not completed.
 * 
 * 
 * 
 * Credits (list of people/things that I used their work):
 * Graphics
 * Bullet, Shrapnel     |   http://pngimg.com/upload/snowflakes_PNG7545.png
 * Exploder             |   http://icons.iconarchive.com/icons/arrioch/christmas/128/snowball-icon.png
 * 
 * Code
 * World, Actor         |   Greenfoot
 * RectangleText        |   Mr. Cohen
 * 
 * 
 * 
 * @author Jason Yuen
 * @version v105
 */
public class Game extends World
{

    private final String LEVEL_DIRECTORY_FORMAT = "levels/%d.txt";
    private final int MAX_LEVEL = 3;
    private final int CURRENT_LEVEL;
    private final int COLUMN_COUNT = 31;
    private final int ROW_COUNT = 12;
    private final int BLOCK_WIDTH = 30;
    private final int BLOCK_HEIGHT = 30;
    private final int PIXEL_SPEED_DEFAULT;
    private final int PIXEL_SPEED_SLOW;                                 // currently serves no purpose
    private final int PIXEL_COUNTDOWN_SPEED = 35;                       // technically the number of milliseconds between frames

    private InfoBar infoBar;
    private final int BAR_HEIGHT = 40;

    private Scanner levelLoad;
    private int movementCountdown;                                      // countdown to actual movement
    private int columnsRemaining;
    private int pixelSpeed;                                             // number of milliseconds needed to advance one pixel
    private int pixelCountdown = 0;                                     // an act() will decrease this by PIXEL_SPEED_COUNTDOWN
    private int columnCountdown = BLOCK_WIDTH;                          // number of pixels left in the first column

    protected Player player;
    private int playerScore = 0;
    private final int PENALTY;

    private BlackRectangle fadeLeft;
    private BlackRectangle fadeRight;

    private Block[][] blockMap = new Block[COLUMN_COUNT][ROW_COUNT];    // the blocks that are on the screen

    private HashSet<Enemy> enemies = new HashSet();                     // all the enemies are neatly stored here

    /**
     * Constructor for a Game.
     * Reads the level number and the player penalty (penalty is not a negative number).
     */
    public Game(int level, int penalty)
    {
        // Create a new world with 930x400 cells with a cell size of 1x1 pixels.
        // it is assumed that the first number is COLUMN_COUNT*BLOCK_WIDTH
        // and the second number is BLOCK_HEIGHT*ROW_COUNT+BAR_HEIGHT
        super(930, 400, 1);

        //if (getWidth() != COLUMN_COUNT*BLOCK_WIDTH || getHeight() != BLOCK_HEIGHT*ROW_COUNT+BAR_HEIGHT) {
        //    System.err.println("Incorrect dimensions detected.");
        //    Greenfoot.stop();
        //}

        // set the order in which actors are drawn
        setPaintOrder(RectangleText.class, BlackRectangle.class, Player.class, Enemy.class, Block.class);

        // build the black bars to hide the edges of the screen
        // build left rectangle
        fadeLeft = new BlackRectangle(BLOCK_WIDTH/2, BLOCK_HEIGHT*ROW_COUNT);
        addObject(fadeLeft, BLOCK_WIDTH/4, (BLOCK_HEIGHT*ROW_COUNT)/2+BAR_HEIGHT);
        // build right rectangle
        fadeRight = new BlackRectangle(BLOCK_WIDTH/2, BLOCK_HEIGHT*ROW_COUNT);
        addObject(fadeRight, getWidth()-BLOCK_WIDTH/4, (BLOCK_HEIGHT*ROW_COUNT)/2+BAR_HEIGHT);

        // check if level number exists
        CURRENT_LEVEL = level;

        if (CURRENT_LEVEL > MAX_LEVEL) {
            // game was completed already
            // should not be possible to get here
            System.err.println("Game was already completed!");
            System.err.println(String.format("Trying to load level %d but the maximum supported level is %d!", CURRENT_LEVEL, MAX_LEVEL));
            Greenfoot.stop();
        }

        // otherwise, set a base score for the player
        PENALTY = penalty;
        playerScore = CURRENT_LEVEL * 1000 - PENALTY;

        // load the level into memory
        try {
            File tempFile = new File(String.format(LEVEL_DIRECTORY_FORMAT, CURRENT_LEVEL));
            levelLoad = new Scanner(tempFile);
        } catch (Exception e) {
            System.err.println("Level " + CURRENT_LEVEL + " was not found!");
            System.err.println("This directory does not exist: " + String.format(LEVEL_DIRECTORY_FORMAT, CURRENT_LEVEL));
            Greenfoot.stop();
        }

        // get info about the level's structure
        PIXEL_SPEED_DEFAULT = levelLoad.nextInt();
        PIXEL_SPEED_SLOW = levelLoad.nextInt();
        pixelSpeed = PIXEL_SPEED_DEFAULT;
        movementCountdown = levelLoad.nextInt();

        // get number of columns in the level
        columnsRemaining = levelLoad.nextInt();
        if (columnsRemaining < COLUMN_COUNT) {
            System.err.println("Not enough columns in this level!");
            System.err.println("Levels require at least " + COLUMN_COUNT + " columns but only found " + columnsRemaining + " column(s)!");
            Greenfoot.stop();
        }

        // load the columns now
        loadBeginningColumns();

        // make a new player
        player = new Player(this);
        addObject(player, 200, 300);

        // initialize the info bar
        infoBar = new InfoBar(this, BAR_HEIGHT);
        updateInfoBar();
    }

    /**
     * Performs this every in-game tick:
     * Check if the level is completed.
     * Check if the pixels should be shifted a little to the left.
     * Update the info bar at the top.
     */
    public void act()
    {
        // check if the level is done
        if (columnsRemaining == 0) {
            // end this level
            doneLevel();
        }
        else {
            // continue running the game
            if (movementCountdown > 0) {
                // player still has to wait at the beginning of the level
                movementCountdown -= PIXEL_COUNTDOWN_SPEED;
            }
            else {
                // update the side scroll
                pixelCountdown -= PIXEL_COUNTDOWN_SPEED;
                
                // keep advancing one pixel while it is possible
                while (pixelCountdown < 0) {
                    // start pushing everything back by one pixel
                    pixelCountdown += pixelSpeed;
                    columnCountdown--;
                    pushBackPixel();

                    // check if column is gone
                    if (columnCountdown <= 0) {
                        // get new column
                        loadColumn();

                        // update player score
                        playerScore += CURRENT_LEVEL;
                        updateInfoBar();

                        // reset column pixel counter
                        columnCountdown = BLOCK_WIDTH;
                    }
                }
            }

            // check if the player pressed the exit button in the info bar
            if (infoBar.pressedExit()) {
                die(0);
            }
        }
    }

    /**
     * A simple method to update the info bar of the game.
     * This method is a universal way for any object to update the game's info bar.
     */
    public void updateInfoBar()
    {
        infoBar.updateInfo(playerScore, player.getLives());
    }

    /**
     * This method is run once in the world's constructor.
     * Load all of the columns at the beginning of the level.
     * This code has a slight advantage over running loadColumn() COLUMN_COUNT times.
     */
    private void loadBeginningColumns()
    {
        // read column by column
        for (int col=0; col<COLUMN_COUNT; col++) {
            columnsRemaining--;

            // set everything in this column to null
            for (int row=0; row<ROW_COUNT; row++) {
                blockMap[col][row] = null;
            }

            // start reading objects from the file
            int k = levelLoad.nextInt();
            for (int counter=0; counter<k; counter++) {
                // get object id
                int id = levelLoad.nextInt();
                if (id == 1) {
                    // regular block
                    // get position
                    int row = levelLoad.nextInt();
                    blockMap[col][row] = new Block("block1.png");
                    addObject(blockMap[col][row], BLOCK_WIDTH*(col+1), getHeight() - BLOCK_HEIGHT*row - BLOCK_HEIGHT/2);
                }
                else {
                    getObjectFromFile(id);
                }
            }
        }
    }

    /**
     * This method is run every time a new column is needed.
     * All of the blocks in blockMap get shifted, and a new column is read from the file and inserted.
     * When an enemy is encountered in the level file, getObjectFromFile() is called to interpret the information properly.
     */
    private void loadColumn()
    {
        columnsRemaining--;

        // delete the blocks in the first column
        for (int row=0; row<ROW_COUNT; row++) {
            if (blockMap[0][row] != null) {
                // delete this block
                //System.out.println("Hi");
                removeObject(blockMap[0][row]);
            }
        }

        // shift everything by one
        for (int col=0; col<COLUMN_COUNT-1; col++) {
            for (int row=0; row<ROW_COUNT; row++) {
                blockMap[col][row] = blockMap[col+1][row];
            }
        }

        // set new column to null
        for (int row=0; row<ROW_COUNT; row++) {
            blockMap[COLUMN_COUNT-1][row] = null;
        }

        // read new column's info
        int k = levelLoad.nextInt();
        for (int i=0; i<k; i++) {
            // get object id
            int id = levelLoad.nextInt();
            if (id == 1) {
                // regular block
                // get position
                int row = levelLoad.nextInt();
                blockMap[COLUMN_COUNT-1][row] = new Block("block1.png");
                addObject(blockMap[COLUMN_COUNT-1][row], getWidth(), getHeight() - BLOCK_HEIGHT*row - BLOCK_HEIGHT/2);
            }
            else {
                getObjectFromFile(id);
            }
        }
    }

    /**
     * Given the object id, read the object and put the info into an array.
     * Then send the info to loadObject() for parsing.
     */
    private void getObjectFromFile(int id)
    {
        //System.out.println("Loading object id "+id);
        if (id == 2) {
            // regular bullet
            int[] info = new int[4];
            info[0] = getWidth();
            for (int i=1; i<4; i++) {
                info[i] = levelLoad.nextInt();
            }
            // add the bullet
            loadObject(id, info);
        }
        else if (id == 3) {
            // regular exploder
            int[] info = new int[4];
            info[0] = getWidth();
            for (int i=1; i<4; i++) {
                info[i] = levelLoad.nextInt();
            }
            // add the exploder
            loadObject(id, info);
        }
        else {
            // unknown object
            System.err.println("Did not recognize object id " + id);
            System.err.println("Crashing is likely to occur soon");
        }
    }

    /**
     * The method is given an object in the form of an array (array is like a blueprint).
     * Then the method initializes the actor and puts it into the Game world.
     */
    public void loadObject(int id, int[] info)
    {
        if (id == 2) {
            // regular bullet
            Bullet b = new Bullet(this, info[0], info[1], info[2], info[3]);
            enemies.add(b);
            addObject(b, info[0], info[1]);
        }
        else if (id == 3) {
            // regular exploder
            Exploder e = new Exploder(this, info[0], info[1], info[2], info[3]);
            enemies.add(e);
            addObject(e, info[0], info[1]);
        }
        else if (id == 4) {
            // regular shrapnel
            Shrapnel s = new Shrapnel(this, info[0], info[1], info[2], info[3]);
            enemies.add(s);
            addObject(s, info[0], info[1]);
            s.act();
        }
    }

    /**
     * Get all the actors in the level and push them back by one pixel.
     * Includes: player, all enemies, all map blocks
     */
    public void pushBackPixel() {
        // player
        player.pushBackPixel();

        // enemies
        for (Enemy e : enemies) {
            e.pushBackPixel();
        }

        // map blocks
        for (int col=0; col<COLUMN_COUNT; col++) {
            for (int row=0; row<ROW_COUNT; row++) {
                if (blockMap[col][row] != null) {
                    blockMap[col][row].pushBackPixel();
                }
            }
        }
    }

    /**
     * You have an actor in this Game world.
     * The actor moves around, and you want it to also collide with the map blocks.
     * Then provide the actor's bounding coordinates, and request the amount of (x, y) movement.
     * Give this method all this info, and it will return the amount of actual (x, y) movement,
     * and a return code (collide with wall/player?).
     */
    public int[] getNewPosition(int actorUp, int actorDown, int actorLeft, int actorRight, int deltaX, int deltaY, boolean hitsPlayer) {
        /*
         * the first four variables define a box
         * drag the box by (deltaX, deltaY) until it hits something, which means it is done
         * 
         * hitsPlayer is whether this object is concerned about hitting the player
         * if it hits the player, then the returnCode includes the 4 bit
         * 
         * this method returns [newX, newY, returnCode]
         * (newX, newY) is the actual dragging that can happen
         * returnCode is in binary, and all possible return codes include:
         * 000, 001, 010, 011, 100, 101, 110, 111
         * the 4 bit --> (only matters if hitsPlayer is true) this object has hit the player
         * the 2 bit --> the blocks are hit on the up/down side
         * the 1 bit --> the blocks are hit on the left/right side
         */

        int moveX = 0;
        int moveY = 0;
        boolean hitX = false;
        boolean hitY = false;
        boolean stopX = (deltaX == 0);
        boolean stopY = (deltaY == 0);
        boolean playerTouch = false;

        // keep looping until there is no reason to loop
        // in each loop, the "actor" is shifted one pixel
        while (!(stopX && stopY)) {
            if (hitsPlayer) {
                // check if this object hits the player
                if (player.touchObject(actorUp+moveY, actorDown+moveY, actorLeft+moveX, actorRight+moveY)) {
                    playerTouch = true;
                }
            }

            /* 
             * move the x coordinate if available
             * either stopY is true
             * or the second option:
             * 
             * if Math.abs(slope) of (moveX, moveY) is greater than (deltaX, deltaY)
             * then the program considers moveX instead of moveY
             * since not enough sideways movement has happened
             * with one problem: moveY/moveX and deltaY/deltaX may not be defined
             * Math.abs(moveY/moveX) >= Math.abs(deltaY/deltaX)
             * Math.abs(deltaX*moveY) >= Math.abs(deltaY*moveX)
             * second is equivalent, except that it is always defined
             */
            if ((!stopX) && (stopY || Math.abs(deltaX*moveY) >= Math.abs(deltaY*moveX)) ) {
                if (0 < deltaX && moveX < deltaX) {
                    // hit right
                    //if ((actorRight - columnCountdown + moveX + 1) % BLOCK_WIDTH == 0) {
                    // check if certain blocks are hit
                    hitX = checkSegment(actorRight+moveX+1, actorUp+moveY, actorRight+moveX+1, actorDown+moveY);
                    stopX = hitX;
                    //}
                    if (!stopX) {
                        moveX++;
                    }
                }
                else if (deltaX < 0 && deltaX < moveX) {
                    // hit left
                    //if ((actorLeft - columnCountdown + moveX) % BLOCK_WIDTH == 0) {
                    // check if certain blocks are hit
                    hitX = checkSegment(actorLeft+moveX-1, actorUp+moveY, actorLeft+moveX-1, actorDown+moveY);
                    stopX = hitX;
                    //}
                    if (!stopX) {
                        moveX--;
                    }
                }
                else break;
            }
            // otherwise, try moving the y coordinate if available
            else if (!stopY) {
                if (0 < deltaY && moveY < deltaY) {
                    // ceiling hit
                    if ((actorUp + moveY + 1) % BLOCK_HEIGHT == 0) {
                        // check if certain blocks are hit
                        hitY = checkSegment(actorLeft+moveX, actorUp+moveY+1, actorRight+moveX, actorUp+moveY+1);
                        stopY = hitY;
                    }
                    if (!stopY) {
                        moveY++;
                    }
                }
                else if (deltaY < 0 && deltaY < moveY) {
                    // floor hit
                    if ((actorDown + moveY) % BLOCK_HEIGHT == 0) {
                        // check if certain blocks are hit
                        hitY = checkSegment(actorLeft+moveX, actorDown+moveY-1, actorRight+moveX, actorDown+moveY-1);
                        stopY = hitY;
                    }
                    if (!stopY) {
                        moveY--;
                    }
                }
                else break;
            }
            else break;
        }

        // interpret results
        int[] boxCollision = new int[] {moveX, moveY, 0};
        
        // interpret the return code
        if (hitX) boxCollision[2]|=1;
        if (hitY) boxCollision[2]|=2;
        if (playerTouch) boxCollision[2]|=4;
        
        // done
        return boxCollision;
    }

    /**
     * A method that is very important to getNewPosition()
     * Checks if a line segment overlaps with any map blocks.
     */
    private boolean checkSegment(int x1, int y1, int x2, int y2) {
        // treats the segment as a rectangle and checks if it intersects a map block
        // get the actual indices that this segment intersects
        // between columns [colBegin, colEnd]
        // between rows [rowBegin, rowEnd]
        int colBegin = Math.min((x1-columnCountdown+BLOCK_WIDTH/2)/BLOCK_WIDTH, (x2-columnCountdown+BLOCK_WIDTH/2)/BLOCK_WIDTH);
        int colEnd = Math.max((x1-columnCountdown+BLOCK_WIDTH/2)/BLOCK_WIDTH, (x2-columnCountdown+BLOCK_WIDTH/2)/BLOCK_WIDTH);
        int rowBegin = Math.min(y1/BLOCK_HEIGHT, y2/BLOCK_HEIGHT);
        int rowEnd = Math.max(y1/BLOCK_HEIGHT, y2/BLOCK_HEIGHT);

        // check if these bounds go out of bounds
        if (colBegin < 0) colBegin = 0;
        if (colEnd >= COLUMN_COUNT) colEnd = COLUMN_COUNT-1;
        if (rowBegin < 0) rowBegin = 0;
        if (rowEnd >= ROW_COUNT) rowEnd = ROW_COUNT-1;

        //System.out.println(String.format("a %d %d %d %d",x1,y1,x2,y2));
        //System.out.println(String.format("b %d %d %d %d %d",columnCountdown,colBegin,colEnd,rowBegin,rowEnd));

        // go through all blocks in this range
        for (int col=colBegin; col<=colEnd; col++) {
            for (int row=rowBegin; row<=rowEnd; row++) {
                if (blockMap[col][row] != null) return true;
            }
        }

        // true was not returned, so there was no intersection
        return false;
    }

    /**
     * Given an enemy.
     * Safely remove the enemy from the Game world.
     */
    public void removeEnemy(Enemy enemy)
    {
        enemies.remove(enemy);
        removeObject(enemy);
    }

    /**
     * Go to the next level, or complete the game if no next level exists.
     */
    private void doneLevel()
    {
        // check if game is done
        if (CURRENT_LEVEL == MAX_LEVEL) {
            // game is done
            Greenfoot.delay(50);
            Greenfoot.setWorld(new DeathScreen(CURRENT_LEVEL, playerScore, true));
        }
        else {
            // go to the next level
            System.out.println("Level " + CURRENT_LEVEL + " was completed!");
            Greenfoot.setWorld(new Game(CURRENT_LEVEL + 1, PENALTY));
        }
    }

    /**
     * Method called when the player is dead or the game is finished.
     * Either way, the effect is the same. In the end, the DeathScreen world is initialized.
     */
    public void die(int delay)
    {
        // update info bar
        updateInfoBar();

        // pause for a while
        Greenfoot.delay(delay);

        // go the the death screen
        Greenfoot.setWorld(new DeathScreen(CURRENT_LEVEL, playerScore, false));
    }
}
