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
    }

    public void update(GameManager manager) {

        gameState = manager;

        if (HP <= 0) {
            gameState.toDelete.add(this);
        }

        yVel += 1.5;
        //movement handlers also handle collisions
        UpDownMovement();
        LeftRightMovement();
       // System.out.println("xVel: " + xVel);

        xLoc += xVel;
        yLoc += yVel;

        hitBox.x = xLoc;
        hitBox.y = yLoc;
    }

    public void render(Graphics g) {

        //g.setColor(Color.green);
        //g.fillRect(xLoc, yLoc, width, height);

        g.drawImage(image, xLoc, yLoc, width, height, null);
        g.setColor(Color.black);
        if (direction) {
            g.fillRect(xLoc + 6*width/8, yLoc + height/3, 20, 3 );
        } else {
            g.fillRect(xLoc - 6*width/8, yLoc + height/3, 20, 3 );
        }



//        String ammoCount = "Ammo: " + ammo;
//        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
//        g.setColor(Color.BLACK); // Here
//        g.drawString(ammoCount, 0,0);

    }

    private void LeftRightMovement() { //smooth movement
        if (keyLeft && keyRight || !keyLeft && !keyRight) {
            xVel *= 0.75;
        } else if (keyLeft && !keyRight) {
            xVel -= 1;
        } else if (keyRight && !keyLeft) {
            xVel += 1;
        }
        handleHorizontalCollision();
        horizontalLimiters();
    }

    //caps speed in horizontal directions
    private void horizontalLimiters() {
        if (xVel < 0.75 && xVel > 0) {
            xVel = 0;
        }
        if (xVel > -0.75 && xVel < 0) {
            xVel = 0;
        }
        if (xVel > 6.5) {
            xVel = 6.5;
        }
        if (xVel < -6.5)
            xVel = -6.5;
    }

    private void UpDownMovement() {
        if (keyUp && keyDown || !keyUp && !keyDown) {
        } else if (keyUp && !keyDown && touchingGround) {
            yVel -= 17;
            touchingGround = false;
        } else if (keyDown && !keyUp) {
            yVel += 0.5;
        }
        handleVerticalCollisions();
        verticalLimiters();
    }

    private void verticalLimiters() {
        if (yVel >= 10) {
            yVel = 10;
        }
    }

    //check if it will collide rather than dealing with collisions after colliding, if we check after it collides it is
    // ambiguous as to which dimensions collisions handler we should use

    private void handleHorizontalCollision() {

        Projectile p = new Projectile(1, 1, false, 0, false);

        boolean goingRight = false;

        if (xVel > 0) {
            goingRight = true;
        }

        Rectangle temp = new Rectangle((int) (hitBox.x + xVel), hitBox.y, hitBox.width, hitBox.height);
        for (Sprite element : gameState.sprites) {
            if (element.hitBox.intersects(temp)) {
                if (element.getClass() != p.getClass() && !element.equals(this)) {
                    if (goingRight) {
                        xVel = 0;
                        xLoc = element.hitBox.x - width;
                        hitBox.x = xLoc;
                        break;
                    } else {
                        xVel = 0;
                        xLoc = element.hitBox.x + element.hitBox.width;
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
                if (element.getClass() != p.getClass() && !element.equals(this)) {
                    if (goingDown) {
                        yLoc = element.hitBox.y - hitBox.height;
                        hitBox.y = yLoc;
                        yVel = 0;
                        touchingGround = true;
                        break;
                    } else {
                        yLoc = element.hitBox.y + element.hitBox.height;
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


    public void takeDamage(int damage) {
        HP -= damage;
    }

    public void knockBack(double force) {
        xVel += force;
    }
}
