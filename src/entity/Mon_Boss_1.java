package entity;

import main.GamePanel;
import main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Mon_Boss_1 extends Entity {
    private final int trapdoorCol = 12;
    private final int trapdoorRow = 11; // anchor góc trên trái của trapdoor 3x3
    private int targetX, targetY;

    private final int[][] waypoints = {
        {12,  8},  // 0 = BẮC
        {15, 11},  // 1 = ĐÔNG  
        {12, 14},  // 2 = NAM
        { 9, 11},  // 3 = TÂY
    };
    private int targetWaypointIdx = 2;

    public Mon_Boss_1(GamePanel gp, int col, int row) {
        super(gp);
        this.worldX = col * gp.tileSize;
        this.worldY = row * gp.tileSize;
        this.direction = "down";
        this.speed = 10;
        this.life = 100; // boss có 100 HP
        this.invicible=false;

        // solidArea khớp 3x3 tile
        solidArea = new Rectangle(0, 0, gp.tileSize * 3, gp.tileSize * 3);
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;

        getImage();
    }

    public void getImage() {
        left1    = setupBoss("/monster/Boss_1/Left(1)");
        left2    = setupBoss("/monster/Boss_1/Left(1)");
        right1  = setupBoss("/monster/Boss_1/Left(1)");
        right2  = setupBoss("/monster/Boss_1/Left(1)");
        up1    = setupBoss("/monster/Boss_1/Left(1)");
        up2    = setupBoss("/monster/Boss_1/Left(1)");
        down1  = setupBoss("/monster/Boss_1/Left(1)");
        down2  = setupBoss("/monster/Boss_1/Left(1)");
    }

    private BufferedImage setupBoss(String path) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path + ".png"));
            image = uTool.scaleImage(image, gp.tileSize * 3, gp.tileSize * 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void setAction() {
        int tdCX = (trapdoorCol + 1) * gp.tileSize;
        int tdCY = (trapdoorRow + 1) * gp.tileSize;
        int dx = gp.player.worldX - tdCX;
        int dy = gp.player.worldY - tdCY;

        if (Math.abs(dx) > Math.abs(dy)) {
            targetWaypointIdx = (dx > 0) ? 1 : 3;
        } else {
            targetWaypointIdx = (dy > 0) ? 2 : 0;
        }

        int targetX = waypoints[targetWaypointIdx][0] * gp.tileSize;
        int targetY = waypoints[targetWaypointIdx][1] * gp.tileSize;

        if (Math.abs(worldX - targetX) > speed) {
            direction = (worldX < targetX) ? "right" : "left";
        } else {
            worldX = targetX;
            if (Math.abs(worldY - targetY) > speed) {
                direction = (worldY < targetY) ? "down" : "up";
            } else {
                worldY = targetY;
                direction = "down";
            }
        }
    }

    @Override
    public void update() {
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);

        if (!collisionOn) {
            switch (direction) {
                case "up":    worldY -= speed; break;
                case "down":  worldY += speed; break;
                case "left":  worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        if(invicible == true){
            invicibleCounter++;

            if(invicibleCounter > 30){ // khoảng 0.5 giây
                invicible = false;
                invicibleCounter = 0;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
        //speed = 10;
    }

    @Override
    public void draw(Graphics2D g2) {
        // Nhấp nháy khi invincible: bỏ qua vẽ mỗi 5 frame
         if (invicible && (invicibleCounter / 5) % 2 == 1) return;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize * 3 > gp.player.worldX - gp.player.screenX &&
            worldX < gp.player.worldX + gp.player.screenX + gp.tileSize &&
            worldY + gp.tileSize * 3 > gp.player.worldY - gp.player.screenY &&
            worldY < gp.player.worldY + gp.player.screenY + gp.tileSize) {

            BufferedImage image;
            switch (direction) {
                case "up":    image = (spriteNum == 1) ? up1    : up2;    break;
                case "left":  image = (spriteNum == 1) ? left1  : left2;  break;
                case "right": image = (spriteNum == 1) ? right1 : right2; break;
                default:      image = (spriteNum == 1) ? down1  : down2;  break;
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize * 3, gp.tileSize * 3, null);

            // ── THANH MÁU ──────────────────────────────────────────────
            int barW     = gp.tileSize * 3;       // rộng bằng sprite boss
            int barH     = 10;                     // chiều cao thanh
            int barX     = screenX;
            int barY     = screenY - barH - 6;     // trên đầu boss, cách 6px

            int maxLife  = 100;
            int fillW    = (int)((life / (double) maxLife) * barW);
            fillW = Math.max(0, Math.min(fillW, barW));

            // Nền đỏ tối
            g2.setColor(new java.awt.Color(100, 0, 0));
            g2.fillRoundRect(barX, barY, barW, barH, 6, 6);

            // Phần máu còn lại (đỏ tươi → vàng → xanh theo HP)
            float ratio = life / (float) maxLife;
            java.awt.Color fillColor;
            if (ratio > 0.5f) {
                fillColor = new java.awt.Color(0, 200, 50);        // xanh lá
            } else if (ratio > 0.25f) {
                fillColor = new java.awt.Color(220, 180, 0);       // vàng
            } else {
                fillColor = new java.awt.Color(220, 40, 40);       // đỏ
            }
            g2.setColor(fillColor);
            if (fillW > 0)
                g2.fillRoundRect(barX, barY, fillW, barH, 6, 6);

            // Viền trắng
            g2.setColor(java.awt.Color.WHITE);
            g2.setStroke(new java.awt.BasicStroke(1.5f));
            g2.drawRoundRect(barX, barY, barW, barH, 6, 6);
            g2.setStroke(new java.awt.BasicStroke(1f)); // reset stroke
        }
    }
}