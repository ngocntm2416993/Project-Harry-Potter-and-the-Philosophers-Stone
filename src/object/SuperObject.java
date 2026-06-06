package object;

import entity.Player;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import main.GamePanel;

public class SuperObject {

    public BufferedImage image;
    public String name = "";
    public String dialogText = "";
    public String[] dialogs = null;
    public int dialogIndex = 0;
    public String hintText   = "";

    public boolean collision = false;
    public int worldX, worldY;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0, solidAreaDefaultY = 0;

    public int drawSizeW = 1;
    public int drawSizeH = 1;

    public int proximityRange = 80;

    public void syncSolidArea(int tileSize) {
        solidArea.width  = tileSize * drawSizeW;
        solidArea.height = tileSize * drawSizeH;
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        int drawW = gp.tileSize * drawSizeW;
        int drawH = gp.tileSize * drawSizeH;

        if (worldX + drawW > gp.player.worldX - gp.player.screenX &&
            worldX         < gp.player.worldX + gp.player.screenX + gp.tileSize &&
            worldY + drawH > gp.player.worldY - gp.player.screenY &&
            worldY         < gp.player.worldY + gp.player.screenY + gp.tileSize) {
            if (image != null)
                g2.drawImage(image, screenX, screenY, drawW, drawH, null);
        }
    }

    public boolean hasProximityHint() {
        return !dialogText.isEmpty() || !hintText.isEmpty();
    }

    public String getProximityHint() {
        return hintText.isEmpty() ? dialogText : hintText;
    }

    public void onInteract(GamePanel gp, Player player) {
        if (!dialogText.isEmpty()) {
            gp.ui.setDialog(name, dialogText);
            gp.gameState = gp.dialogState;
        }
    }

    public void onContact(GamePanel gp, Player player) {}
}