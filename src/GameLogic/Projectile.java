package GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Projectile extends Sprite{

    private GameManager gameState;

    private int damage;

    private boolean player1;

    //true is positive (right), false is negative (left)
    public Projectile(int x, int y, boolean direction, int damage, boolean player) { //boolean determines which player projectile belongs to
        super(x,y, 15, 10);
        player1 = player;
        this.damage = damage;
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
                if (!element.equals(this) && element.getClass() != this.getClass() && player1 && !element.equals(gameState.player1)) {
                    xVel = 0;
                    gameState.toDelete.add(this);
                    if (element.equals(gameState.player2)) {
                        gameState.player2.HP = gameState.player2.HP - damage;
                    }
                } else if (!element.equals(this) && element.getClass() != this.getClass() && !player1 && !element.equals(gameState.player2)) {
                    xVel = 0;
                    gameState.toDelete.add(this);
                    if (element.equals(gameState.player1)) {
                        gameState.player1.HP = gameState.player1.HP - damage;
                    }
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
