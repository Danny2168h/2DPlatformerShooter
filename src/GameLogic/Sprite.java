package GameLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Sprite {
    
    protected int xLoc;
    protected int yLoc;
    protected double xVel;
    protected double yVel;

    protected int height; // used to draw the image
    protected int width;

    protected Rectangle hitBox; //usually same width and height however may choose to use smaller hitBox

    protected BufferedImage image;
    
    public Sprite(int x, int y, int width, int height) {
        xLoc = x;
        yLoc = y;

        this.height = height;
        this.width = width;
        
        xVel = 0;
        yVel = 0;

        hitBox = new Rectangle(x, y, width, height);
    }
    
    public abstract void update(GameManager manager);

    public abstract void render(Graphics g);
}
