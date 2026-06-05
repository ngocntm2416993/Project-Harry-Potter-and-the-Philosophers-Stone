package object;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;
import entity.Player;

public class OBJ_HP extends SuperObject {

    private int spawnDelay; // frame delay trước khi hiển thị (60fps)

    public OBJ_HP(int tileSize, int delaySeconds) {
        name      = "Hồi máu";
        collision = false;
        spawnDelay = delaySeconds * 60;

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

    // Constructor cũ không delay — giữ để không break chỗ khác
    public OBJ_HP(int tileSize) {
        this(tileSize, 0);
    }

    @Override
    public void draw(java.awt.Graphics2D g2, GamePanel gp) {
        if (spawnDelay > 0) {
            spawnDelay--;
            return; // chưa đến giờ → không vẽ, không tương tác
        }
        collision = true; // bật collision khi đã hiển thị
        super.draw(g2, gp);
    }

    @Override
    public void onContact(GamePanel gp, Player player) {
        if (spawnDelay > 0) return; // chưa hiển thị thì không nhặt được
        player.HP = Math.min(player.HP + 50, 200);
        gp.ui.showMessage("Hồi máu +50 HP!");
        gp.playSE(1);
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == this) { gp.obj[i] = null; break; }
        }
    }

    @Override
    public void onInteract(GamePanel gp, Player player) {
        onContact(gp, player);
    }

    @Override
    public boolean hasProximityHint() { return spawnDelay <= 0; }

    @Override
    public String getProximityHint() {
        return "[F] Nhặt lọ thuốc màu đỏ — hồi 50 máu";
    }
}