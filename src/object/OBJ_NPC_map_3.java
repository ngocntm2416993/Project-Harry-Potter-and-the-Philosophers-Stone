package object;

import entity.Player;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_NPC_map_3 extends SuperObject {
    public OBJ_NPC_map_3(int tileSize) {
        this.name       = "Old Man";
        this.dialogs = new String[] {
                "vào OBJ_NPC_map_3 đẻ thêm thoại", "chia ra làm npc 1,2,3,4,....", "nếu được thay bằng mỗi map 1 người sẽ hay hơn"
        };
        this.hintText   = "[F] Xem hướng dẫn";
        this.collision  = true;
        this.drawSizeW  = 1;
        this.drawSizeH  = 1;
        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = tileSize;
        solidArea.height = tileSize;
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_down_1.png"));
        } catch (IOException e) { e.printStackTrace(); }
    }
    @Override
    public void onInteract(GamePanel gp, Player player){
        if (dialogIndex < dialogs.length) {
            gp.ui.setDialog(name, dialogs[dialogIndex]);
            gp.gameState = gp.dialogState;
            dialogIndex++;
        } else {
            dialogIndex = 0; // Reset lại
            gp.gameState = gp.playState;
        }
    }
}
