package GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HealthPack extends Sprite{

    private int heal;

    public HealthPack(int x, int y, int width, int height) {
        super(x, y, width, height);
        heal = 20;
        String basepath = new File("").getAbsolutePath();
        try {
            image = ImageIO.read(new File(basepath + "\\src\\Resources\\healthPack.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public HealthPack() {
        super(0,0,0,0);
    }

    @Override
    public void update(GameManager manager) {
    }

    @Override
    public void render(Graphics g) {
            g.drawImage(image, xLoc,yLoc,width,height,null);
    }

    public int getHp() {
        return heal;
    }
}

