package GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Projectile extends Sprite{

    private GameManager gameState;

    private int damage;

    private boolean player1;

    private boolean direction;

    //true is positive (right), false is negative (left)
    public Projectile(int x, int y, boolean direction, int damage, boolean player) { //boolean determines which player projectile belongs to
        super(x,y, 15, 10);
        player1 = player;
        this.damage = damage;
        this.direction = direction;
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

        double force = 7.0;

        // Rectangle temp = new Rectangle((int) (hitBox.x + xVel), hitBox.y, width, height);
        for (Sprite element : gameState.sprites) {
            if (element.hitBox.intersects(hitBox)) {
                if (!element.equals(this) && element.getClass() != this.getClass() && player1 && !element.equals(gameState.player1)) {
                    xVel = 0;
                    gameState.toDelete.add(this);
                    if (element.equals(gameState.player2)) {
                        gameState.player2.takeDamage(damage);
                        if (direction) {
                            gameState.player2.knockBack(force);
                        } else {
                            gameState.player2.knockBack(force * -1);
                        }
                    }
                } else if (!element.equals(this) && element.getClass() != this.getClass() && !player1 && !element.equals(gameState.player2)) {
                    xVel = 0;
                    gameState.toDelete.add(this);
                    if (element.equals(gameState.player1)) {
                        gameState.player1.takeDamage(damage);
                        if (direction) {
                            gameState.player1.knockBack(force);
                        } else {
                            gameState.player1.knockBack(force * -1);
                        }
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
