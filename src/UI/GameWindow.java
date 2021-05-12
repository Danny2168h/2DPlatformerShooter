package UI;

import GameLogic.GameLoop;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class GameWindow extends JFrame implements Observer {
    private static int WIDTH = 640;
    private static int HEIGHT = WIDTH / 12 * 9;
    private static String TITLE = "Platformer Game";
    private mainMenuPanel mainMenuPanel;
    private GameLoop gameLoop;
    private EscapeMenu escape;

    public GameWindow() {
        this.setTitle(TITLE);

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        mainMenuPanel = new mainMenuPanel(this);
        this.add(mainMenuPanel);
        this.setVisible(true);
    }

    public void startGame(boolean s) {
        this.remove(mainMenuPanel);
        //mainMenuPanel = null;
        this.repaint();
        if (s) {
            gameLoop = new GameLoop(true, this);
        } else {
            gameLoop = new GameLoop(false, this);
        }
        this.add(gameLoop);
        this.setVisible(true);
        gameLoop.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        gameLoop.thread.suspend(); // basically a block
        this.remove(gameLoop);
        gameLoop.resetPlayer();
        this.repaint();
        escape = new EscapeMenu(this);
        this.add(escape);
        this.setVisible(true);
    }

    public void mainMenu() {
        gameLoop.thread.resume();
        gameLoop.stop();
     //   gameLoop = null;
        this.remove(escape);
       // escape = null;
        this.repaint();
        mainMenuPanel = new mainMenuPanel(this);
        this.add(mainMenuPanel);
        this.setVisible(true);
    }

    public void resumeGame() {
        this.remove(escape);
      // escape = null;
        this.repaint();//basically unblock
        this.add(gameLoop);
        gameLoop.thread.resume();
        gameLoop.requestFocus();
        this.setVisible(true);
    }
}
