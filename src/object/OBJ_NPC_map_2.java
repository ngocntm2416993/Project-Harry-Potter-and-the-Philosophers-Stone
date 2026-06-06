package object;

import entity.Player;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_NPC_map_2 extends SuperObject {
    public OBJ_NPC_map_2(int tileSize) {
        this.name       = "Người lùn";
        this.dialogs = new String[] {
                "Cửa ải này các quân cờ sẽ bắt đầu sống dậy",
                "Chúng sẽ là trở ngại mà ngươi phải đối đầu",
                "Hãy vượt qua chúng",
                "Xin hãy lưu ý rằng\nĐừng để bước chân của chúng dẫm phải"
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
            image = ImageIO.read(getClass().getResourceAsStream("/npc/NPC_2.png"));
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
