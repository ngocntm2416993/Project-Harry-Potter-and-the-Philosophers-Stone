package object;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;
import entity.Player;

public class OBJ_Potion_Slash extends SuperObject {

    public OBJ_Potion_Slash(int tileSize) {
        name      = "Lọ thuốc Slash";
        collision = false;

        try {
            UtilityTool uTool = new UtilityTool();
            image = uTool.scaleImage(
                    ImageIO.read(getClass().getResourceAsStream("/object/potion/thuocxanhduong.png")),
                    tileSize, tileSize
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        solidArea         = new Rectangle(0, 0, tileSize, tileSize);
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
    }

    @Override
    public void onContact(GamePanel gp, Player player) {
        if (player.hasSlash) {
            gp.ui.showMessage("Đã có chiêu Slash rồi!");
            return;
        }
        player.hasSlash = true;
        gp.ui.showMessage("Đã học được chiêu Slash!");
        gp.playSE(1);

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == this) {
                gp.obj[i] = null;
                break;
            }
        }
    }

    @Override
    public void onInteract(GamePanel gp, Player player) {
        onContact(gp, player);
    }

    @Override
    public boolean hasProximityHint() { return true; }

    @Override
    public String getProximityHint() {
        return "[F] Nhặt lọ thuốc màu xanh dương — mở khóa chiêu Slash";
    }
}