package GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observer;

public class GameLoop extends Canvas implements Runnable {

    private static int WIDTH = 640;
    private static int HEIGHT = WIDTH / 12 * 9;
    private static String TITLE = "Platformer Game";

    private int updateNum;

    public Thread thread;
    public boolean running = false;

    private BufferedImage backgroundImage;

    private GameManager gameManager;

    //in constructor is where we add elements to the game manager

    public GameLoop(boolean s, Observer observer) {

        gameManager = new GameManager(observer);
        if (s) {
            gameManager.addPlayer1(new Player(30, 30, 20, 40));
        } else {
            gameManager.addPlayer1(new Player(30, 30, 20, 40));
            gameManager.addPlayer2(new Player(100, 100, 20, 40));
        }
        setUpGame();
        //new UI.GameWindow(WIDTH, HEIGHT, TITLE, this);
    }

    private void setUpGame() {
        int WALL_WIDTH = 100;
        int WALL_HEIGHT = 25;
        for (int i = 0; i < WIDTH; i += WALL_WIDTH - 20) {
            gameManager.addSprite(new Walls(i, 360, WALL_WIDTH, WALL_HEIGHT));
        }

        gameManager.addSprite(new Boundary(0,0, 1, HEIGHT));
        gameManager.addSprite(new Boundary(WIDTH - 50, 0, 1, HEIGHT));

        gameManager.addSprite(new Walls(300, 200, WALL_WIDTH, WALL_HEIGHT));
        gameManager.addSprite(new Walls(180, 290, WALL_WIDTH, WALL_HEIGHT));
        this.addKeyListener(new KeyHandler(gameManager));

        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\Danny\\2DPlatformerShooter\\src\\Resources\\background.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void start() {
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //other game loop, unrestricted frame rate, however highly taxing on the CPU
//
//    @Override
//    public void run() {
//        this.requestFocus();
//        long lastTime = System.nanoTime();
//        double numTicks = 60.0;
//        double timePerTick = 1000000000 / numTicks; //divide 1 sec in nano secs by 60 ticks;
//        double delta = 0;
//        long timer = System.currentTimeMillis();
//        int frames = 0;
//
//        while(running) {
//            long now = System.nanoTime();
//            delta += (now - lastTime) / timePerTick; // this controls it so that we only call update 60 times in 1 second as long as rendering and ticking do not take 500 ms
//            lastTime = now;
//            while (delta >= 1) { // if delta less than 1 (amount of time it took to render and update previous) we do not update which allows for frames to render uncapped
//                update();
//                delta -= 1;
//            }
//
//            if (running) {
//                render();
//            }
//
//            frames++;
//
//            if (System.currentTimeMillis() - timer > 1000) {
//                timer += 1000;
//                System.out.println("FPS: " + frames + "  " + "Updates: " + updateNum);
//                frames = 0;
//                updateNum = 0;
//            }
//        }
//        stop();
//
//    }


    // restricted frame rate and update time, game engine is effected by frame rate
    @Override
    public void run() {
        this.requestFocus();
        long now;
        long updateTime;
        long wait;
        int frames = 0;
        long timer = System.currentTimeMillis();

        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        while (running) {
            now = System.nanoTime();

            update();
            render();

            frames++;

            updateTime = System.nanoTime() - now;
            wait = (OPTIMAL_TIME - updateTime) / 1000000;

            if (wait > 0) {

                try {
                    thread.sleep(wait);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames + "  " + "Updates: " + updateNum);
                frames = 0;
                updateNum = 0;
            }

        }
    }


    private void render() {

        BufferStrategy buffer = this.getBufferStrategy();
        if (buffer == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = buffer.getDrawGraphics();


        g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);

        //g.setColor(Color.blue);
        //g.fillRect(0,0, WIDTH, HEIGHT);

        gameManager.renderEach(g);

        g.dispose();
        buffer.show();
    }

    private void update() {
        gameManager.updateEach();
        updateNum++;
    }

    public void resetPlayer() {
       gameManager.resetPlayers();
    }
}
