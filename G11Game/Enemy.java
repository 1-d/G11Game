import greenfoot.*;

/**
 * A superclass for all enemies.
 * This class implements some important methods so they don't have to be repeated.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class Enemy extends Actor
{
    public Game game;

    // bounds on the enemy
    protected int actorDown;
    protected int actorUp;
    protected int actorLeft;
    protected int actorRight;

    /**
     * Move this enemy back one pixel.
     */
    public void pushBackPixel()
    {
        actorLeft--;
        actorRight--;
    }

    /**
     * Called when this enemy dies.
     */
    public void die()
    {
        // remove this actor
        game.removeEnemy(this);
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
}
