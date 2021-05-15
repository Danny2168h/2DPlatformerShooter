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
//        try {
//            image = ImageIO.read(new File(basepath + "\\src\\Resources\\health-pack-png-4.png"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    @Override
    public void update(GameManager manager) {
    }

    @Override
    public void render(Graphics g) {
            g.setColor(Color.red);
            g.fillRect(xLoc,yLoc,width,height);
    }

    public int getHp() {
        return heal;
    }
}

