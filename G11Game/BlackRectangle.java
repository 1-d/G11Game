import greenfoot.*;
import java.awt.Color;

/**
 * This actor only displays pure black.
 * Somehow manages to be even sadder than the Block class.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class BlackRectangle extends Actor
{

    GreenfootImage RectangleImage;

    public BlackRectangle(int x, int y)
    {
        // set dimensions of rectangle
        RectangleImage = new GreenfootImage(x, y);

        // fill rectangle with black
        RectangleImage.setColor(Color.BLACK);
        RectangleImage.fill();

        // done
        setImage(RectangleImage);
    }
}
