package GameLogic;

import UI.GameWindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class GameManager extends Observable {

    private final GameWindow gameWindow;
    public ArrayList<Sprite> sprites = new ArrayList<>();
    public Player player1;
    public Player player2;
    public ArrayList<Sprite> toDelete = new ArrayList<>();
    private boolean playerShoot1 = false;
    private boolean playerShoot2 = false;
    private static int COOLDOWN = 5;
    private int counter1 = COOLDOWN;
    private int counter2 = COOLDOWN;


    public GameManager(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }


    public void updateEach() {
        projectileHandler();

        for (Sprite element : sprites) {
            element.update(this);
        }
        clearColliders();
    }

    private void projectileHandler() {
        if (counter1 != 0) {
            counter1--;
        }

        if (counter2 != 0) {
            counter2--;
        }

        if (playerShoot1 && counter1 == 0 && player1.ammo > 0 && player1.HP > 0) {
            Projectile projectile;
            if (player1.direction) {
                projectile = new Projectile(player1.xLoc + player1.width - 4,
                        player1.yLoc - 10 + player1.height / 2, true, 10, true);
            } else {
                projectile = new Projectile(player1.xLoc - 4,
                        player1.yLoc - 10 + player1.height / 2, false, 10, true);
            }
            sprites.add(projectile);
            counter1 = COOLDOWN;
            player1.ammo--;
        }

        if (playerShoot2 && counter2 == 0 && player2.ammo > 0 && player2 .HP > 0) {
            Projectile projectile;
            if (player2.direction) {
                projectile = new Projectile(player2.xLoc + player2.width - 4,
                        player2.yLoc - 10 + player2.height / 2, true, 10, false);
            } else {
                projectile = new Projectile(player2.xLoc - 4,
                        player2.yLoc - 10 + player2.height / 2, false, 10, false);
            }
            sprites.add(projectile);
            counter2 = COOLDOWN;
            player2.ammo--;
        }
    }

    public void renderEach(Graphics g) {

        for (Sprite element : sprites) {
            element.render(g);
        }
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public void clearColliders() {
        for (Sprite sprite : toDelete) {
            sprites.remove(sprite);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            player1.keyUp = true;
        } else if (e.getKeyChar() == 'a') {
            player1.keyLeft = true;
        } else if (e.getKeyChar() == 's') {
            player1.keyDown = true;
        } else if (e.getKeyChar() == 'd') {
            player1.keyRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player2.keyUp = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player2.keyLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player2.keyDown = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player2.keyRight = true;
        }
        if (e.getKeyChar() == 'j') {
            playerShoot1 = true;
        }
        if (e.getKeyChar() == '2') {
            playerShoot2 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameWindow.pressEsc();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            player1.keyUp = false;
        } else if (e.getKeyChar() == 'a') {
            player1.keyLeft = false;
        } else if (e.getKeyChar() == 's') {
            player1.keyDown = false;
        } else if (e.getKeyChar() == 'd') {
            player1.keyRight = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player2.keyUp = false; //need to check for null field
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player2.keyLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player2.keyDown = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player2.keyRight = false;
        }
        if (e.getKeyChar() == 'j') {
            playerShoot1 = false;
        }

        if (e.getKeyChar() == '2') {
            playerShoot2 = false;
        }
    }

    public void addPlayer1(Player player) {
        this.player1 = player;
        sprites.add(player);
    }

    public void addPlayer2(Player player) {
        this.player2 = player;
        sprites.add(player);
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void resetPlayers() {

        playerShoot1 = false;
        playerShoot2 = false;

        if (player1 != null) {
            player1.keyDown = false;
            player1.keyUp = false;
            player1.keyLeft = false;
            player1.keyRight = false;
        }
        if (player2 != null) {
            player2.keyDown = false;
            player2.keyUp = false;
            player2.keyLeft = false;
            player2.keyRight = false;
        }
    }
}
