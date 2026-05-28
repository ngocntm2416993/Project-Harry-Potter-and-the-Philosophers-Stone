package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_BookTable extends SuperObject {

    public OBJ_BookTable(String imagePath, int drawW, int drawH, String dialog, int tileSize) {
        this.name       = "Bàn Sách";
        this.dialogText = dialog;
        this.hintText   = "[F] Xem bàn";
        this.collision  = true;
        this.drawSizeW  = drawW;
        this.drawSizeH  = drawH;

        // Offset y để player đi sát vào mặt bàn được
        int offsetY = tileSize / 2; // 24px — bỏ phần trống phía trên
        solidArea.x      = 4;
        solidArea.y      = offsetY;
        solidArea.width  = tileSize * drawW - 8;
        solidArea.height = tileSize * drawH - offsetY;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        proximityRange = tileSize * 2;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) { e.printStackTrace(); }
    }
}
