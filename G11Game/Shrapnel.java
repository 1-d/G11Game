import greenfoot.*;

/**
 * A Shrapnel is an Enemy that is affected of gravity.
 * When the Shrapnel hits a wall or player, it dies.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class Shrapnel extends Enemy
{

    private final int ACTOR_SIZE = 10;

    private double deltaX;
    private double deltaY;
    private double ACCELERATION = -0.3;

    private int[] newPositionInfo;

    /**
     * Constructor for the shrapnel.
     */
    public Shrapnel(Game g, int x, int y, int dX, int dY)
    {
        game = g;

        // get actor's initial position
        actorLeft = x-ACTOR_SIZE/2;
        actorRight = x+ACTOR_SIZE/2;
        actorDown = y-ACTOR_SIZE/2;
        actorUp = y+ACTOR_SIZE/2;

        deltaX = dX;
        deltaY = dY;
    }

    /**
     * Every act, the shrapnel will try to move.
     * If the shrapnel collides with anything, it will die.
     */
    public void act() {
        // let the shrapnel fall
        deltaY += ACCELERATION;

        // get actor's movement
        newPositionInfo = game.getNewPosition(actorUp, actorDown, actorLeft, actorRight, roundAway(deltaX), roundAway(deltaY), true);

        // actual movement
        actorLeft += newPositionInfo[0];
        actorRight += newPositionInfo[0];
        actorUp += newPositionInfo[1];
        actorDown += newPositionInfo[1];
        setLocation((actorLeft+actorRight)/2, game.getHeight() - (actorUp+actorDown)/2);

        // check if this bullet hit the player
        if ((newPositionInfo[2] & 4) == 4) {
            hitPlayer();
        }

        // speed changes
        if ((newPositionInfo[2] & 1) == 1) {
            // blocks hit on left/right side
            die();
        }
        if ((newPositionInfo[2] & 2) == 2) {
            // hit floor or ceiling
            die();
        }

        // check if this bullet is not on the screen
        if (actorRight <= 0 || actorUp <= 0) {
            die();
        }
    }

    /**
     * Called when the shrapnel has hit the player.
     */
    public void hitPlayer()
    {
        System.out.println("Hit player!");
        game.player.loseLife();
        die();
    }
}
