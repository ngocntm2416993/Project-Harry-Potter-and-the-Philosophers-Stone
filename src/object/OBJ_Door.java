package object;

import entity.Entity;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Door extends SuperObject {
    public int targetMap;
    public int bossRequiredMap = -1; // -1 là map không có boss
    public boolean interactable = true;
    public OBJ_Door(String imagePath, int targetMap,  int tileSize) {
        this.name       = "Cửa";
        this.targetMap  = targetMap;
        this.hintText   = "[F] Đi đến màn " + targetMap;
        this.collision  = false;
        this.drawSizeW  = 3;
        this.drawSizeH  = 3;
        int inset = tileSize / 4;
        solidArea.x      = inset;
        solidArea.y      = inset;
        solidArea.width  = tileSize * 3 - inset * 2;
        solidArea.height = tileSize * 3 - inset * 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        proximityRange   = tileSize * 2;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) { e.printStackTrace(); }
    }
    public void setInteractable(boolean state) {
        this.interactable = state;
    }
    @Override
    public void onInteract(main.GamePanel gp, entity.Player player) {
        if (bossRequiredMap != -1) {
            boolean bossStillAlive = false;

            // Duyệt danh sách quái để xem còn Boss không
            for (Entity m : gp.monster) {
                // Kiểm tra nếu Boss tồn tại và đang ở đúng màn yêu cầu
                if (m != null && gp.currentMap == bossRequiredMap) {
                    bossStillAlive = true;
                    break;
                }
            }

            if (bossStillAlive) {
                gp.ui.showMessage("Cánh cửa đã bị khóa bởi quyền năng của Boss!");
                return; // Ngắt hàm, không thực hiện chuyển map
            }
        }
        if (interactable) {
            gp.changeMap(targetMap);
        } else {
            gp.ui.showMessage("Hiện tại cửa bị khóa.");
        }
    }
}