package object;

import entity.Player;
import main.GamePanel;

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
    @Override
    public void onInteract(GamePanel gp, Player player){
        boolean boss4Alive = false;
        for (entity.Entity m : gp.monster) {
            if (m != null && m instanceof entity.Mon_Boss_4) {
                boss4Alive = true;
                break;
            }
        }
        if (boss4Alive) {
            // Khi boss chưa chết: Chỉ hiện thoại bình thường
            gp.ui.setDialog(this.name, this.dialogText);
            gp.gameState = gp.dialogState;
        } else {
            gp.gameState = gp.dialogState;
            gp.ui.resetEndgameAlpha();
            gp.gameState = gp.endGameState;
        }
    }
}