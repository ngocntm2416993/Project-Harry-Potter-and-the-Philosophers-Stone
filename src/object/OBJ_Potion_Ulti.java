package object;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;
import entity.Player;

public class OBJ_Potion_Ulti extends SuperObject {

    public OBJ_Potion_Ulti(int tileSize) {
        name      = "Nấm kì lạ";
        collision = false;

        try {
            UtilityTool uTool = new UtilityTool();
            BufferedImage raw = ImageIO.read(
                getClass().getResourceAsStream("/object/mushroom/Golden_Brittlegill.png")
            );
            if (raw != null) image = uTool.scaleImage(raw, tileSize, tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        solidArea = new Rectangle(0, 0, tileSize, tileSize);
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
    }

    @Override
    public void onContact(GamePanel gp, Player player) {
        if (player.hasUlti) {
            gp.ui.showMessage("Đã có chiêu Ulti rồi!");
            return;
        }
        player.hasUlti = true;
        gp.ui.showMessage("Đã học được chiêu Ulti!");
        gp.playSE();

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
        return "Lại gần để nhặt";
    }
}