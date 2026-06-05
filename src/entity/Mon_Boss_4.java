package entity;

import main.GamePanel;
import main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.random.*;

public class Mon_Boss_4  extends Entity{
    private int targetWaypointIdx = 2;
    private static final int VISIBLE_FRAMES   = 180; // 3s × 60fps
    private static final int INVISIBLE_FRAMES = 60;  // 1s ẩn trước khi teleport
    private int visibleTimer   = VISIBLE_FRAMES;
    private boolean isVisible  = true;
    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 1000;

    public Mon_Boss_4(GamePanel gp, int col, int row) {
        super(gp);
        this.worldX = col * gp.tileSize;
        this.worldY = row * gp.tileSize;
        this.direction = "down";
        this.speed     = 2;
        this.life = 500;

        // solidArea — bắt buộc phải có
        solidArea = new java.awt.Rectangle(0, 0, gp.tileSize * 3, gp.tileSize * 3);
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
        getImage();
    }

    // Vùng spawn ngẫu nhiên (col, row) — chỉnh lại theo map của bạn
    private static final int[][] SPAWN_TILES = {
        {2,4}, {5,4}, {13,4}, {20,4}, {8,8}, {20,8}, {2,11}, {14,11}
    };

    private final int[][] waypoints = {
        {12,  8},  // 0 = BẮC
        {15, 11},  // 1 = ĐÔNG  
        {12, 14},  // 2 = NAM
        { 9, 11},  // 3 = TÂY
    };

    public void getImage(){
        left1    = setupBoss("/monster/Boss_4/wizard_default");
        left2    = setupBoss("/monster/Boss_4/wizard_death(1)");
        right1  = setupBoss("/monster/Boss_4/wizard_default");
        right2  = setupBoss("/monster/Boss_4/wizard_death(1)");
        up1    = setupBoss("/monster/Boss_4/wizard_default");
        up2    = setupBoss("/monster/Boss_4/wizard_death(1)");
        down1  = setupBoss("/monster/Boss_4/wizard_default");
        down2  = setupBoss("/monster/Boss_4/wizard_death(1)");
    }
    
    private BufferedImage setupBoss(String path) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path + ".png"));
            image = uTool.scaleImage(image,gp.tileSize*2, gp.tileSize * 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void setAction(){
        visibleTimer--;

        if (isVisible) {
            // Đang hiện: chạy logic waypoint bình thường
            if (visibleTimer <= 0) {
                // Hết 3s → ẩn đi
                isVisible = false;
                visibleTimer = INVISIBLE_FRAMES;
                speed = 0; // đứng yên khi ẩn
            } else {
                // Logic di chuyển waypoint của bạn
                speed = 2; // hoặc tốc độ bạn muốn
                int dx = gp.player.worldX - worldX;
                int dy = gp.player.worldY - worldY;
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
        } else {
            // Đang ẩn: chờ rồi teleport
            if (visibleTimer <= 0) {
                teleportRandom();
                isVisible = true;
                visibleTimer = VISIBLE_FRAMES;
            }
        }
    }

    // ── Teleport đến 1 ô random trong SPAWN_TILES ─────────────────
    private void teleportRandom() {
        Random random = new Random();
        int[] tile = SPAWN_TILES[random.nextInt(SPAWN_TILES.length)];
        worldX = tile[0] * gp.tileSize;
        worldY = tile[1] * gp.tileSize;
    }

    @Override
    public void update() {
        setAction();
        if (!isVisible) return;

            // CHECK VA CHẠM VỚI PLAYER
        boolean hitPlayer = gp.cChecker.checkPlayer(this);
        if (hitPlayer) {
            long now = System.currentTimeMillis();
            if (now - lastDamageTime > DAMAGE_COOLDOWN) {
                lastDamageTime = now;
                gp.player.HP -= 20;
                gp.player.invicible = true;
                gp.player.invicibleCounter = 0;
                gp.ui.showMessage("Boss tấn công! -20 HP");
                gp.playSE();
                if (gp.player.HP <= 0) {
                    gp.player.HP = 0;
                    gp.gameState = gp.gameOverState;
                }
            }
        }

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkPlayer(this);
        gp.cChecker.checkEntity(this, gp.npc);

        if (!collisionOn) {
            switch (direction) {
                case "up":    worldY -= speed; break;
                case "down":  worldY += speed; break;
                case "left":  worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        // ── Invincible counter ────────────────────────────────────────────
        if (invicible) {
            invicibleCounter++;
            if (invicibleCounter > 30) {
                invicible = false;
                invicibleCounter = 0;
            }
        }

        // 30 frame cuối trước khi ẩn → dùng ảnh 2 (trắng)
        // còn lại luôn dùng ảnh 1
        if (visibleTimer < 10) {
            spriteNum = 2;
        } else {
            spriteNum = 1;
        }
    }

    @Override
    public void draw(java.awt.Graphics2D g2) {
        if (!isVisible) return;
        // Nhấp nháy khi invincible
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
            g2.drawImage(image, screenX, screenY, gp.tileSize * 2, gp.tileSize * 4, null);

            // ── Thanh máu ─────────────────────────────────────────────────────
            int barW    = gp.tileSize * 2;
            int barH    = 10;
            int barX    = screenX;
            int barY    = screenY - barH - 6;
            int fillW   = (int)((life / (double) maxLife) * barW);
            fillW = Math.max(0, Math.min(fillW, barW));

            g2.setColor(new java.awt.Color(100, 0, 0));
            g2.fillRoundRect(barX, barY, barW, barH, 6, 6);

            float ratio = life / (float) maxLife;
            g2.setColor(ratio > 0.5f
                ? new java.awt.Color(0, 200, 50)
                : ratio > 0.25f
                    ? new java.awt.Color(220, 180, 0)
                    : new java.awt.Color(220, 40, 40));
            if (fillW > 0) g2.fillRoundRect(barX, barY, fillW, barH, 6, 6);

            g2.setColor(java.awt.Color.WHITE);
            g2.setStroke(new java.awt.BasicStroke(1.5f));
            g2.drawRoundRect(barX, barY, barW, barH, 6, 6);
            g2.setStroke(new java.awt.BasicStroke(1f));
        }
    }

}
