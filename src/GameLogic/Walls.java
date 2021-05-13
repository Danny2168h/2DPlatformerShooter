package GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Walls extends Sprite {

    public Walls(int x, int y, int width, int height) {
        super(x, y, width, height);

        try {
            image = ImageIO.read(new File("C:\\Users\\Danny\\2DPlatformerShooter\\src\\Resources\\platform.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // increases accuracy of platform hitbox
        hitBox = new Rectangle(x + 6, y + 12, width - 15, height - 15 );

    }

    @Override
    public void update(GameManager manager) {
    }

    @Override
    public void render(Graphics g) {

        g.drawImage(image, xLoc, yLoc, width, height, null);

        //g.setColor(Color.black);
        //g.fillRect(xLoc, yLoc, width, height);
    }
}
