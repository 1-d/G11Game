import greenfoot.*;

/**
 * The Block class really only stores an image. Pretty sad.
 * True story.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class Block extends Actor
{

    /**
     * Constructor for the block.
     * Takes in a image file directory and sets this as its image.
     */
    public Block(String imageDirectory)
    {
        setImage(imageDirectory);
    }

    /**
     * Move this block back one pixel.
     */
    public void pushBackPixel()
    {
        setLocation(getX()-1, getY());
    } 
}
