package GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Player extends Sprite {

    public boolean keyLeft = false;
    public boolean keyRight = false;
    public boolean keyUp = false;
    public boolean keyDown = false;

    public int ammo = 100;
    public boolean direction = true; //true is right, false is left

    private boolean touchingGround = false;

    private GameManager gameState;

    public int HP = 100;

    public Player(int x, int y, int width, int height) {

        super(x, y, width, height);

        try {
            image = ImageIO.read(new File("C:\\Users\\Danny\\2DPlatformerShooter\\src\\Resources\\mokey.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        hitBox = new Rectangle(x, y, width, 26);
    }

    public void update(GameManager manager) {

        gameState = manager;

        if (HP <= 0) {
            gameState.toDelete.add(this);
        }

        yVel += 1;
        LeftRightMovement(); //movement handlers also handle collisions
        UpDownMovement();

        xLoc += xVel;
        yLoc += yVel;

        if (xVel > 0) {
            direction = true;
        } else if (xVel == 0){
            //dont change direction
        } else if (xVel < 0) {
            direction = false;
        }

        hitBox.x = xLoc;
        hitBox.y = yLoc;
    }

    public void render(Graphics g) {

       // g.setColor(Color.green);
        //g.fillRect(xLoc, yLoc, width, height);

        g.drawImage(image, xLoc, yLoc, width, height, null);

//        String ammoCount = "Ammo: " + ammo;
//        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
//        g.setColor(Color.BLACK); // Here
//        g.drawString(ammoCount, 0,0);

    }

    private void LeftRightMovement() { //smooth movement
        if (keyLeft && keyRight || !keyLeft && !keyRight) {
            xVel *= 0.8;
        } else if (keyLeft && !keyRight) {
            xVel -= 1;
        } else if (keyRight && !keyLeft) {
            xVel += 1;
        }
        handleHorizontalCollision();
        if (xVel < 0.4 && xVel >= 0) {
            xVel = 0;
        }
        if (xVel > -0.4 && xVel <= 0) {
            xVel = 0;
        }
        if (xVel >= 7) {
            xVel = 7;
        }
        if (xVel <= -7)
            xVel = -7;
    }

    private void UpDownMovement() {
        if (keyUp && keyDown || !keyUp && !keyDown) {
        } else if (keyUp && !keyDown && touchingGround) {
            yVel -= 15;
            touchingGround = false;
        } else if (keyDown && !keyUp) {
            yVel += 0.5;
        }
        handleVerticalCollisions();
        if (yVel >= 9) {
            yVel = 9;
        }
    }

    //check if it will collide rather than dealing with collisions after colliding, if we check after it collides it is
    // ambigous as to which dimensions collisions handler we should use

    private void handleHorizontalCollision() {

        Projectile p = new Projectile(1, 1, false, 0, false);

        boolean goingRight = false;

        if (xVel > 0) {
            goingRight = true;
        }
        Rectangle temp = new Rectangle((int) (hitBox.x + xVel), hitBox.y, hitBox.width, hitBox.height);
        for (Sprite element : gameState.sprites) {
            if (element.hitBox.intersects(temp)) {
                if (element.getClass() != p.getClass() && element.getClass() != this.getClass()) {
                    if (goingRight) {
                        xVel = 0;
                        xLoc = element.xLoc - width;
                        hitBox.x = xLoc;
                        break;
                    } else {
                        xVel = 0;
                        xLoc = element.xLoc + element.hitBox.width;
                        hitBox.x = xLoc;
                        break;
                    }
                }
            }
        }
    }

    private void handleVerticalCollisions() {

        Projectile p = new Projectile(1, 1, false, 0, false);

        boolean goingDown = false;

        if (yVel > 0) {
            goingDown = true;
        }
        Rectangle temp = new Rectangle(hitBox.x, (int) (hitBox.y + yVel), hitBox.width, hitBox.height);
        for (Sprite element : gameState.sprites) {
            if (element.hitBox.intersects(temp)) {
                if (element.getClass() != p.getClass() && element.getClass() != this.getClass()) {
                    if (goingDown) {
                        yLoc = element.yLoc - hitBox.height;
                        hitBox.y = yLoc;
                        yVel = 0;
                        touchingGround = true;
                        break;
                    } else {
                        yLoc = element.yLoc + element.hitBox.height;
                        hitBox.y = yLoc;
                        yVel = 0;
                        break;
                    }
                }
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getxLoc() {
        return xLoc;
    }

    public int getyLoc() {
        return yLoc;
    }


}
