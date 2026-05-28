package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Mirror extends SuperObject {

    public OBJ_Mirror(int tileSize) {
        this.name       = "Gương Thần";
        this.dialogText = "Gương phản chiếu một bóng người... không giống bạn.";
        this.hintText   = "[F] Nhìn vào gương";
        this.collision  = true;
        this.drawSizeW  = 4;
        this.drawSizeH  = 4;
        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = tileSize * 4;
        solidArea.height = tileSize * 4;
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
        proximityRange   = tileSize * 2;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/deco/guongthan.png"));
        } catch (IOException e) { e.printStackTrace(); }
    }
}