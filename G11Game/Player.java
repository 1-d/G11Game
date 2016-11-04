import greenfoot.*;

/**
 * The user is able to control the player with this class.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class Player extends Actor
{

    private Game game;

    // all player bounding boxes
    private int playerDown = 90;
    private int playerUp = 110;
    private int playerLeft = 190;
    private int playerRight = 210;

    // movement variables
    private double ACCELERATION_NORMAL = -0.2;
    private double JUMP_SPEED_NORMAL = 5;
    private double HORIZONTAL_SPEED_MAX_NORMAL = 5;
    private double HORIZONTAL_ACCELERATION = 1.2;

    // max jump distance = 17 blocks
    // max jump height = 4 blocks

    private double acceleration = ACCELERATION_NORMAL;
    private double jumpSpeed = JUMP_SPEED_NORMAL;
    private double horizontalSpeedMax = HORIZONTAL_SPEED_MAX_NORMAL;
    private boolean grounded = false;
    private double deltaX = 0;
    private double deltaY = 0;

    // other player info
    private int playerLives = 3;

    private int[] newPositionInfo;

    /**
     * Simple constructor does not require much info.
     * Variables are already initialized.
     */
    public Player(Game g)
    {
        game = g;
    }

    /**
     * Check for any speed changes, like left/right and jumping.
     * Then performs the actual movement.
     * After actual movement, collisions are read and position is updated.
     * Then check if the player is dead.
     */
    public void act() 
    {
        // always press the player downwards
        deltaY += acceleration;

        // separate speed changed by whether or not the player is grounded
        if (grounded) {
            // normal jump
            if (Greenfoot.isKeyDown("up")) deltaY = JUMP_SPEED_NORMAL;

            // left or right movement decay
            if (Greenfoot.isKeyDown("shift")) deltaX = 0;
            else deltaX *= 0.8;

            // left or right movement
            if (Greenfoot.isKeyDown("left")) deltaX -= HORIZONTAL_ACCELERATION;
            if (Greenfoot.isKeyDown("right")) deltaX += HORIZONTAL_ACCELERATION;
        }
        else {

            // slow or speed up the fall
            if (Greenfoot.isKeyDown("up")) deltaY -= acceleration / 2;
            if (Greenfoot.isKeyDown("down")) deltaY += acceleration * 2;

            // left or right movement
            if (Greenfoot.isKeyDown("left")) deltaX -= HORIZONTAL_ACCELERATION/4;
            if (Greenfoot.isKeyDown("right")) deltaX += HORIZONTAL_ACCELERATION/4;
        }

        // max horizontal speed
        if (deltaX > horizontalSpeedMax) deltaX = horizontalSpeedMax;
        else if (deltaX < -horizontalSpeedMax) deltaX = -horizontalSpeedMax;

        // update the player's new location
        newPositionInfo = game.getNewPosition(playerUp, playerDown, playerLeft, playerRight, roundAway(deltaX), roundAway(deltaY), false);

        // actual movement
        playerLeft += newPositionInfo[0];
        playerRight += newPositionInfo[0];
        playerUp += newPositionInfo[1];
        playerDown += newPositionInfo[1];
        setLocation((playerLeft+playerRight)/2, game.getHeight() - (playerUp+playerDown)/2);

        // make sure player doesn't go through the right side
        if (playerRight > game.getWidth()-15) {
            // move player
            playerRight = game.getWidth()-15;
            playerLeft = game.getWidth()-35;

            // collision happened
            newPositionInfo[2] |= 1;
        }

        // speed changes
        // check if grounded
        if (newPositionInfo[1] != 0) {
            grounded = false;
        }
        if ((newPositionInfo[2] & 2) == 2) {
            // blocks hit on up/down side
            // check if grounded now (e.g. didn't hit a ceiling)
            grounded = (deltaY<0);

            if (grounded) deltaY = 0;
            else deltaY = -deltaY/2;
        }
        if ((newPositionInfo[2] & 1) == 1) {
            // blocks hit on left/right side
            deltaX = 0;
        }

        // check if this location means that the player is dead
        if (playerRight <= 0 || playerUp <= 0) {
            game.die(50);
        }
    }

    /**
     * Looks like Java kept this method well-hidden because I can't find it.
     * This method will round a number away from zero.
     */
    public int roundAway(double d)
    {
        // rounds the float away from zero
        // unless d is very close to zero (in this case round to zero)
        // -1.1 --> -2
        // 1.1 --> 2

        // close enough to zero
        if (Math.abs(d) < 0.4) {
            return 0;
        }
        else if (d < 0) {
            //
            return (int) Math.floor(d);
        }
        else {
            return (int) Math.ceil(d);
        }
    }

    /**
     * An enemy will call this often.
     * Checks if the enemy touches the player.
     */
    public boolean touchObject(int actorUp, int actorDown, int actorLeft, int actorRight)
    {
        // check if y coordinates are good
        if ((playerDown<=actorUp && actorUp<=playerUp) || (playerDown<=actorDown && actorUp<=playerDown)) {
            // check if x coordinates are good
            if ((playerLeft<=actorRight && actorRight<=playerRight) || (playerLeft<=actorLeft && actorLeft<=playerRight)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the number of lives in the player.
     */
    public int getLives()
    {
        return playerLives;
    }

    /**
     * Force the player to lose a life.
     * The player can die if the number of lives is zero afterwards.
     */
    public void loseLife()
    {
        // this player will lose a life now
        playerLives--;
        // update the game's info bar
        game.updateInfoBar();

        // check if player is dead now
        if (playerLives == 0) game.die(50);
    }

    /**
     * Move the player back one pixel.
     */
    public void pushBackPixel()
    {
        playerLeft--;
        playerRight--;
    }
}
