package GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Projectile extends Sprite{

    private GameManager gameState;

    //true is positive (right), false is negative (left)
    public Projectile(int x, int y, boolean direction) {
        super(x,y, 15, 10);
        if (direction) {
            xVel = 10;
        } else {
            xVel = -10;
        }

        try {
            image = ImageIO.read(new File("C:\\Users\\Danny\\2DPlatformerShooter\\src\\Resources\\fireball.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(GameManager manager) {
        gameState = manager;
        handleCollision();
        xLoc += xVel;
        hitBox.x = xLoc;
    }

    private void handleCollision() {

       // Rectangle temp = new Rectangle((int) (hitBox.x + xVel), hitBox.y, width, height);
        for (Sprite element : gameState.sprites) {
            if (element.hitBox.intersects(hitBox)) {
                if (!element.equals(this) && element.getClass() != this.getClass()
                        && !element.equals(gameState.player1)) {
                    xVel = 0;
                    gameState.toDelete.add(this);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
       // g.setColor(Color.red);
        //g.fillRect(xLoc, yLoc, width, height);

        g.drawImage(image, xLoc, yLoc, width, height, null);
    }
}
