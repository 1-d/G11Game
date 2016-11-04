import greenfoot.*;
import java.awt.Color;

/**
 * A short piece of code that makes text less annoying to implement.
 * 
 * @author Jason Yuen
 * @version v105
 */
public class RectangleText extends Actor
{
    // important colours
    final Color RECTANGLE_FOREGROUND = Color.BLACK;
    final Color RECTANGLE_BACKGROUND = Color.WHITE;
    final Color RECTANGLE_BORDER = Color.GRAY;
    final Color TRANSPARENT = new Color(0,0,0,0);

    // image to be displayed
    GreenfootImage RectangleImage;

    /**
     * Constructor for RectangleText.
     * Get all the info, then create the RectangleImage immediately.
     */
    public RectangleText(String text, int fontSize, int width, int height)
    {
        updateText(text, fontSize, width, height);
    }

    /**
     * This method changes the text on the RectangleImage.
     */
    public void updateText(String text, int fontSize, int width, int height)
    {
        // make the text
        GreenfootImage textImage = new GreenfootImage(text, fontSize, RECTANGLE_FOREGROUND, RECTANGLE_BACKGROUND);

        // make a new image for this Rectangle
        RectangleImage = new GreenfootImage(width, height);

        // make background transparent
        RectangleImage.setColor(TRANSPARENT);
        RectangleImage.fill();

        // fill the Rectangle
        RectangleImage.setColor(RECTANGLE_BACKGROUND);
        RectangleImage.fillRect(0,0,RectangleImage.getWidth()-1, RectangleImage.getHeight()-1);
        // draw the Rectangle outline
        RectangleImage.setColor(RECTANGLE_BORDER);
        RectangleImage.drawRect(0,0,RectangleImage.getWidth()-1, RectangleImage.getHeight()-1);

        // put the text into this Rectangle
        RectangleImage.drawImage(textImage, (width-textImage.getWidth())/2, (height-textImage.getHeight())/2);

        // done
        setImage(RectangleImage);
    }
}
