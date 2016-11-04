import greenfoot.*;

/**
 * The Exploder is a type of Enemy that can explode when it dies.
 * It is the most threatening Enemy to the player
 * because it has the potential to instantly kill the player.
 * The Exploder is also ground-loving, so the player must jump over it.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class Exploder extends Enemy
{
    private final int ACTOR_SIZE = 20;
    private final int PIECES;   // number of pieces this enemy explodes into

    private double deltaX;

    // falling variables
    private double deltaY = 0;
    private double ACCELERATION = -0.2;

    private int[] newPositionInfo;

    /**
     * Constructor for the exploder.
     */
    public Exploder(Game g, int x, int y, int dX, int pieces)
    {
        game = g;

        // get actor's initial position
        actorLeft = x-ACTOR_SIZE/2;
        actorRight = x+ACTOR_SIZE/2;
        actorDown = y-ACTOR_SIZE/2;
        actorUp = y+ACTOR_SIZE/2;

        deltaX = dX;
        PIECES = pieces;
    }

    /**
     * Every act, the exploder will try to move.
     * The exploder falls normally, but if it hits a left/right wall,
     * then the exploder will explode and die.
     */
    public void act() 
    {
        // let the exploder fall
        deltaY += ACCELERATION;

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
            die(true);
        }
        if ((newPositionInfo[2] & 2) == 2) {
            // hit floor
            deltaY = 0;
        }

        // check if this bullet hit the player
        if ((newPositionInfo[2] & 4) == 4) {
            hitPlayer();
        }

        // check if this bullet is dead now
        if (actorRight <= 0 || actorUp <= 0) {
            die(true);
        }
    }

    /**
     * Called when the exploder has hit the player.
     */
    public void hitPlayer()
    {
        System.out.println("Hit player!");

        // one piece = one life lost
        for (int i=0; i<PIECES; i++) {
            game.player.loseLife();
        }

        die(false);
    }

    /**
     * Interesting die method.
     * If this exploder dies, then it could explode some Shrapnel enemies.
     */
    public void die(boolean explode)
    {
        // check for explosion
        if (explode) {
            // make new actors
            for (int i=0; i<PIECES; i++) {
                // cos(2*pi/i) and sin(2*pi/i)
                game.loadObject(4, new int[] {(actorLeft+actorRight)/2, (actorUp+actorDown)/2, roundAway(8*Math.cos(2*Math.PI/PIECES*i)), roundAway(8*Math.sin(2*Math.PI/PIECES*i))});
            }
        }

        // remove exploder
        game.removeEnemy(this);
    }
}
