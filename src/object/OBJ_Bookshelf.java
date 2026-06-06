package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Bookshelf extends SuperObject {

    public OBJ_Bookshelf(String imagePath, String dialog, int tileSize) {
        this.name       = "Kệ Sách";
        this.dialogText = dialog;
        this.hintText   = "[F] Đọc sách";
        this.collision  = true;
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