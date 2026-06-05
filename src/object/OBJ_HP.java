package object;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;
import entity.Player;

public class OBJ_HP extends SuperObject {
    private int spawnDelay;

    public OBJ_HP(int tileSize) {
        name      = "Hồi máu";
        collision = false;

        try {
            UtilityTool uTool = new UtilityTool();
            image = uTool.scaleImage(
                ImageIO.read(getClass().getResourceAsStream("/object/potion/thuocdo.png")),
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
        player.HP = Math.min(player.HP + 50, 200); // cộng 50HP, không vượt max
        gp.ui.showMessage("Hồi máu +50 HP!");
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
        return "[F] Nhặt lọ thuốc màu đỏ — hồi 50 máu";
    }
}