package object;

import entity.Player;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_NPC_map_1 extends SuperObject {
    public OBJ_NPC_map_1(int tileSize) {
        this.name       = "Hồn ma";
        this.dialogs = new String[] {
                "Bạn cần vượt qua 4 màn để kết thúc trò chơi",
                "Dùng phím [F] để đánh quái",
                "Ngoài ra có thể dùng [E] và [Q[ để thi triển\nSlash và Ultimate",
                "2 kĩ năng trên\nCần có item kích hoạt",
                "Những item này được đặt rải rác\ntrong các màn chơi",
                "Ngoài ra sẽ có một số item hỗ trợ\nđược đặt rải rác trong màn chơi",
                "Chúc bạn thành công!"
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
            image = ImageIO.read(getClass().getResourceAsStream("/npc/NPC_1.png"));
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
