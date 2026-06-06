package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Scroll extends SuperObject {

    public OBJ_Scroll(String imagePath, String dialog, int tileSize) {
        this.name       = "Cuộn Sách";
        this.dialogText = dialog;
        this.hintText   = "[F] Một là độc dược, dù giấu kỹ càng. Dễ dàng tìm được, bên trái rượu tầm ma (1);";
        this.drawSizeW  = 2;
        this.drawSizeH  = 2;
        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = tileSize * 2;
        solidArea.height = tileSize * 2;
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
        proximityRange   = tileSize * 2;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) { e.printStackTrace(); }
    }
}