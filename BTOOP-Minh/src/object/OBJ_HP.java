package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_HP extends SuperObject{
    public OBJ_HP() {
        name = "HP";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/object/HP.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
