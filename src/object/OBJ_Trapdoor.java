package object;

import entity.Entity;
import entity.Player;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Trapdoor extends SuperObject {
    public int targetMap;
    public int spawnTileX, spawnTileY;
    public int bossRequiredMap = -1;
    public OBJ_Trapdoor(int targetMap, int spawnTileX, int spawnTileY, int tileSize) {
        this.name       = "Cửa Sập";
        this.hintText   = "[F] Đi đến màn " + targetMap;
        this.dialogText = "Một cửa sập dẫn xuống tầng dưới...";
        this.targetMap  = targetMap;
        this.spawnTileX = spawnTileX;
        this.spawnTileY = spawnTileY;
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
            image = ImageIO.read(getClass().getResourceAsStream("/door/trapdoor.png"));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onInteract(GamePanel gp, Player player) {
        // Kiểm tra xem cửa có yêu cầu đánh bại Boss không
        if (bossRequiredMap != -1) {
            boolean bossStillAlive = false;
            for (Entity m : gp.monster) {
                // Kiểm tra nếu Boss tồn tại và thuộc màn đang chặn
                if (m != null && gp.currentMap == bossRequiredMap) {
                    bossStillAlive = true;
                    break;
                }
            }
            if (bossStillAlive) {
                gp.ui.showMessage("Cửa sập đã bị khóa. Hãy đánh bại Boss trước!");
                return; // Dừng lại, không thực hiện lệnh chuyển map
            }
        }
        // Nếu qua được điều kiện trên thì mới chuyển map
        gp.changeMap(targetMap);
    }
}