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
    public boolean shoot = false;

    public boolean direction = true; //true is right, false is left

    private boolean touchingGround = false;

    private GameManager gameState;

    public int HP = 100;

    private Weapon weapon;

    private boolean addedWeapon = false;

  //  public int coolDown = 5;
   // public int counter;
  //  public int ammo = 800;


    public Player(int x, int y, int width, int height) {

        super(x, y, width, height);

        weapon = new Weapon(0,0, 20, 4, 5.0, 10, 10.0, 5, 5, this);

        String basepath = new File("").getAbsolutePath();

        try {
            image = ImageIO.read(new File(basepath + "\\src\\Resources\\mokey.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void update(GameManager manager) {

        gameState = manager;

        if(!addedWeapon) {
            addedWeapon = true;
            gameState.toAdd.add(weapon);
        }

        if (HP <= 0) {
            gameState.toDelete.add(this);
        }

        yVel += 1;
        //movement handlers also handle collisions
        UpDownMovement();
        LeftRightMovement();

        //projectileHandler();

        if (xVel > 0) {
            xLoc = (int) Math.ceil(xLoc + xVel); //java cast from int to double means that positive additions lose values after decimal point
        } else {
            xLoc += xVel;
        }

        yLoc += yVel;

        hitBox.x = xLoc;
        hitBox.y = yLoc;
    }

    public void render(Graphics g) {

        //g.setColor(Color.green);
        //g.fillRect(xLoc, yLoc, width, height);

        g.drawImage(image, xLoc, yLoc, width, height, null);
    }

    private void LeftRightMovement() { //smooth movement
        if (keyLeft && keyRight || !keyLeft && !keyRight) {
            xVel *= 0.75;
        } else if (keyLeft && !keyRight) {
            xVel -= 0.5;
        } else if (keyRight && !keyLeft) {
            xVel += 0.5;
        }
        handleHorizontalCollision();
        horizontalLimiters();
    }

    //caps speed in horizontal directions
    private void horizontalLimiters() {
        if (xVel < 0.50 && xVel > 0) {
            xVel = 0;
        }
        if (xVel > -0.50 && xVel < 0) {
            xVel = 0;
        }
        if (xVel > 6) {
            xVel = 6;
        }
        if (xVel < -6)
            xVel = -6;
    }

    private void UpDownMovement() {
        if (keyUp && keyDown || !keyUp && !keyDown) {
            // do nothing
        } else if (keyUp && !keyDown && touchingGround) {
            yVel -= 13;
            touchingGround = false;
        } else if (keyDown && !keyUp) {
            yVel += 0.8;
        }
        handleVerticalCollisions();
        verticalLimiters();
    }

    private void verticalLimiters() {
        if (yVel >= 9) {
            yVel = 9;
        }
    }


    //check if it will collide rather than dealing with collisions after colliding, if we check after it collides it is
    // ambiguous as to which dimensions collisions handler we should use

    private void handleHorizontalCollision() {

        Projectile p = new Projectile(1, 1, false, 0, 0,0,this);

        boolean goingRight = false;

        if (xVel > 0) {
            goingRight = true;
        }

        Rectangle left = new Rectangle((int) (hitBox.x + xVel), hitBox.y, hitBox.width, hitBox.height);
        Rectangle right = new Rectangle((int) Math.ceil(hitBox.x + xVel), hitBox.y, hitBox.width, hitBox.height);
        for (Sprite element : gameState.sprites) {
            if (goingRight && element.getClass() != p.getClass() && !element.equals(this) && element.hitBox.intersects(right)) {
                xVel = 0;
                xLoc = element.hitBox.x - width;
                hitBox.x = xLoc;
                break;
            } else if (!goingRight && element.getClass() != p.getClass() && !element.equals(this) && element.hitBox.intersects(left)) {
                xVel = 0;
                xLoc = element.hitBox.x + element.hitBox.width;
                hitBox.x = xLoc;
                break;
            }
        }
    }

    private void handleVerticalCollisions() {

        Projectile p = new Projectile(1, 1, false, 0, 0, 0,this);

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


    public void takeDamage(int damage) {
        HP -= damage;
    }

    public void knockBack(double force) {
        xVel += force;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public boolean getDirection() {
        return direction;
    }

    public int getHP() {
        return HP;
    }

    public void refreshedWeapon() {
        addedWeapon = false;
    }

    //    private void projectileHandler() {
//       if (counter != 0) {
//           counter--;
//       }
//
//        if (shoot && counter == 0 && ammo > 0 && HP > 0) {
//            Projectile projectile;
//            if (direction) {
//                projectile = new Projectile(xLoc + width,
//                        yLoc + height / 2, true, 5, 7, 10, this);
//            } else {
//                projectile = new Projectile(xLoc,
//                        yLoc + height / 2, false, 5, 7,10, this);
//            }
//            gameState.toAdd.add(projectile);
//            counter = coolDown;
//            ammo--;
//            gameState.ammoUpdate();
//        }
//    }

}
