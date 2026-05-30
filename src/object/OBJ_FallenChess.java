package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_FallenChess extends SuperObject {

    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 1000;

    public OBJ_FallenChess(int tileSize) {
        this.name       = "Bàn Cờ Đổ";
        this.dialogText = "Một bàn cờ đổ... các quân cờ vương vãi khắp nơi.";
        this.hintText   = "[F] Xem bàn cờ";
        this.collision  = true;
        this.drawSizeW  = 3;
        this.drawSizeH  = 3;
        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = tileSize * 3;
        solidArea.height = tileSize * 3;
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
        proximityRange   = tileSize * 2;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/deco/fallenchess.png"));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onContact(main.GamePanel gp, entity.Player player) {
        long now = System.currentTimeMillis();
        if (now - lastDamageTime > DAMAGE_COOLDOWN) {
            lastDamageTime = now;
            player.HP = Math.max(0, player.HP - 10);
            gp.ui.showMessage("Cờ gây 10 sát thương!");
            gp.playSE(1);
        }
    }
}