package object;

import entity.Player;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_NPC_map_3 extends SuperObject {
    public OBJ_NPC_map_3(int tileSize) {
        this.name       = "Bộ xương";
        this.dialogs = new String[] {
                "Ở cửa ải này, ngươi cần vượt qua ngọn lửa tím\nđể tới cửa ải cuối cùng",
                "Trên bàn là các lọ thuốc\nHãy tìm lọ thuốc đúng để băng qua ngọn lửa",
                "Chọn [S] và [W] để di chuyển lên xuống\nChọn [F] để chọn thuốc cần uống",
                "Các manh mối được rải rác trong đây",
                "Hãy nhớ rằng\nKhông phải lọ thuốc nào cũng đem lại tác dụng tốt",
                "Hãy cẩn trọng khi đưa ra lựa chọn"
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
            image = ImageIO.read(getClass().getResourceAsStream("/npc/NPC_3.png"));
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
