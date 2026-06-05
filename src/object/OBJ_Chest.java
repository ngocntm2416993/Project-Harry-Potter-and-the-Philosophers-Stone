package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Chest extends SuperObject {

    private boolean opened = false;

    public OBJ_Chest(int tileSize) {
        this.name       = "Rương Kho Báu";
        this.dialogText = "Rương chứa một thứ gì đó bên trong...";
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
            image = ImageIO.read(getClass().getResourceAsStream("/object/chest.png"));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onInteract(main.GamePanel gp, entity.Player player) {
        if (opened) return;
        opened = true;
        double roll = Math.random();
        if (roll < 0.5) {
            player.HP = Math.min(player.HP + 50, 200);
            gp.ui.showMessage("Rương: Hồi phục 50 HP!");
        } else {
            player.speed = player.normalSpeed + 4;
            player.speedBoostEndTime = System.currentTimeMillis() + 5000;
            gp.ui.showMessage("Rương: Tăng tốc 5 giây!");
        }
        gp.playSE();
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == this) { gp.obj[i] = null; break; }
        }
    }
}