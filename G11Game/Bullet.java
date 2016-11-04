import greenfoot.*;

/**
 * A Bullet is an Enemy that will bounce back and forth.
 * The Bullet is also the most basic type of enemy.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class Bullet extends Enemy
{
    private final int ACTOR_SIZE = 10;

    private double deltaX;
    private double deltaY;

    private int[] newPositionInfo;

    /**
     * Constructor for the bouncing bullet.
     */
    public Bullet(Game g, int x, int y, int dX, int dY)
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
     * Every act, the bullet will try to move.
     * If the bullet collides, it will bounce off and continue to move at a constant speed.
     */
    public void act()
    {
        // get actor's movement
        newPositionInfo = game.getNewPosition(actorUp, actorDown, actorLeft, actorRight, roundAway(deltaX), roundAway(deltaY), true);

        // actual movement
        actorLeft += newPositionInfo[0];
        actorRight += newPositionInfo[0];
        actorUp += newPositionInfo[1];
        actorDown += newPositionInfo[1];
        setLocation((actorLeft+actorRight)/2, game.getHeight() - (actorUp+actorDown)/2);

        // speed changes
        if ((newPositionInfo[2] & 1) == 1) {
            // blocks hit on left/right side
            deltaX = -deltaX;
        }
        if ((newPositionInfo[2] & 2) == 2) {
            deltaY = -deltaY;
        }

        // check if this bullet hit the player
        if ((newPositionInfo[2] & 4) == 4) {
            hitPlayer();
        }

        // check if this bullet is dead now
        if (actorRight <= 0 || actorUp <= 0) {
            die();
        }
    }

    /**
     * Called when the bullet has hit the player.
     */
    public void hitPlayer()
    {
        System.out.println("Hit player!");
        game.player.loseLife();
        die();
    }

}
