package entity;

import main.GamePanel;
import main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Mon_Boss_1 extends Entity {

    // ── Vùng di chuyển cho phép (tile) ───────────────────────────────────
    private static final int BOUND_LEFT   = 5;
    private static final int BOUND_TOP    = 5;
    private static final int BOUND_RIGHT  = 21;
    private static final int BOUND_BOTTOM = 18;

    // ── Điểm canh giữ trung tâm (pixel) ──────────────────────────────────
    private final int guardX;
    private final int guardY;

    // ── Ngưỡng đuổi (pixel) ───────────────────────────────────────────────
    private static final int CHASE_RANGE  = 10 ; // tile — phát hiện player
    private static final int RETURN_RANGE = 11; // tile — bỏ đuổi

    private enum State { GUARD, CHASE, RETURN }
    private State state = State.GUARD;

    // ── Waypoints tuần tra ────────────────────────────────────────────────
    private final int[][] waypoints = {
        {12,  8},
        {15, 11},
        {12, 14},
        { 9, 11},
    };
    private int targetWaypointIdx = 2;

    // ── Damage ────────────────────────────────────────────────────────────
    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 1000;

    public Mon_Boss_1(GamePanel gp, int col, int row) {
        super(gp);
        this.worldX    = col * gp.tileSize;
        this.worldY    = row * gp.tileSize;
        this.direction = "down";
        this.speed     = 10;
        this.life      = 60;
        this.invicible = false;

        // Tâm vùng bound = trung tâm giữa (10,10)→(19,19)
        guardX = (BOUND_LEFT + BOUND_RIGHT)  / 2 * gp.tileSize;
        guardY = (BOUND_TOP  + BOUND_BOTTOM) / 2 * gp.tileSize;

        solidArea = new Rectangle(0, 0, gp.tileSize * 3, gp.tileSize * 3);
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;

        getImage();
    }

    public void getImage() {
        left1 = left2 = right1 = right2 =
        up1   = up2   = down1  = down2  =
            setupBoss("/monster/Boss_1/Left(1)");
    }

    private BufferedImage setupBoss(String path) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path + ".png"));
            image = uTool.scaleImage(image, gp.tileSize * 3, gp.tileSize * 3);
        } catch (IOException e) { e.printStackTrace(); }
        return image;
    }

    /** Kẹp tọa độ boss trong vùng bound */
    private void clampToBound() {
        int minX = BOUND_LEFT   * gp.tileSize;
        int minY = BOUND_TOP    * gp.tileSize;
        int maxX = (BOUND_RIGHT  - 2) * gp.tileSize; // -2 vì boss rộng 3 tile
        int maxY = (BOUND_BOTTOM - 2) * gp.tileSize;
        worldX = Math.max(minX, Math.min(worldX, maxX));
        worldY = Math.max(minY, Math.min(worldY, maxY));
    }

    /** Player có đang trong vùng bound không */
    private boolean playerInBound() {
        int px = gp.player.worldX / gp.tileSize;
        int py = gp.player.worldY / gp.tileSize;
        return px >= BOUND_LEFT && px <= BOUND_RIGHT
            && py >= BOUND_TOP  && py <= BOUND_BOTTOM;
    }

    @Override
    public void setAction() {
        // Tâm boss
        int bossCX = worldX + solidArea.width  / 2;
        int bossCY = worldY + solidArea.height / 2;

        // Khoảng cách boss → player (tile)
        int dxPlayer = gp.player.worldX - bossCX;
        int dyPlayer = gp.player.worldY - bossCY;
        double distToPlayer = Math.sqrt(dxPlayer * dxPlayer + dyPlayer * dyPlayer)
                              / gp.tileSize;

        // Khoảng cách boss → điểm canh giữ
        int dxGuard = guardX - worldX;
        int dyGuard = guardY - worldY;

        // ── Chuyển trạng thái ─────────────────────────────────────────────
        switch (state) {
            case GUARD:
                // Chỉ đuổi khi player vào trong vùng bound VÀ đủ gần
                if (playerInBound() && distToPlayer <= CHASE_RANGE)
                    state = State.CHASE;
                break;
            case CHASE:
                // Dừng đuổi khi player ra khỏi bound HOẶC quá xa
                if (!playerInBound() || distToPlayer > RETURN_RANGE)
                    state = State.RETURN;
                break;
            case RETURN:
                if (Math.abs(dxGuard) < speed * 2 && Math.abs(dyGuard) < speed * 2) {
                    worldX = guardX;
                    worldY = guardY;
                    state  = State.GUARD;
                }
                break;
        }

        // ── Hành động theo trạng thái ─────────────────────────────────────
        switch (state) {
            case CHASE: {
                speed = 3;
                if (Math.abs(dxPlayer) > Math.abs(dyPlayer))
                    direction = (dxPlayer > 0) ? "right" : "left";
                else
                    direction = (dyPlayer > 0) ? "down" : "up";
                break;
            }
            case RETURN: {
                speed = 3;
                if (Math.abs(dxGuard) > Math.abs(dyGuard))
                    direction = (dxGuard > 0) ? "right" : "left";
                else
                    direction = (dyGuard > 0) ? "down" : "up";
                break;
            }
            case GUARD: {
                speed = 2;
                int dx = gp.player.worldX - guardX;
                int dy = gp.player.worldY - guardY;
                if (Math.abs(dx) > Math.abs(dy))
                    targetWaypointIdx = (dx > 0) ? 1 : 3;
                else
                    targetWaypointIdx = (dy > 0) ? 2 : 0;

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
                break;
            }
        }
    }

    private void pushPlayer() {
        java.awt.Rectangle bossRect = new java.awt.Rectangle(
            worldX + solidAreaDefaultX,
            worldY + solidAreaDefaultY,
            solidArea.width, solidArea.height
        );
        java.awt.Rectangle playerRect = new java.awt.Rectangle(
            gp.player.worldX + gp.player.solidAreaDefaultX,
            gp.player.worldY + gp.player.solidAreaDefaultY,
            gp.player.solidArea.width, gp.player.solidArea.height
        );

        if (!bossRect.intersects(playerRect)) return;

        int overlapLeft  = (playerRect.x + playerRect.width)  - bossRect.x;
        int overlapRight = (bossRect.x   + bossRect.width)    - playerRect.x;
        int overlapUp    = (playerRect.y + playerRect.height)  - bossRect.y;
        int overlapDown  = (bossRect.y   + bossRect.height)    - playerRect.y;

        int minX = Math.min(overlapLeft, overlapRight);
        int minY = Math.min(overlapUp,   overlapDown);

        if (minX < minY) {
            if (overlapLeft < overlapRight) gp.player.worldX -= overlapLeft;
            else                            gp.player.worldX += overlapRight;
        } else {
            if (overlapUp < overlapDown)    gp.player.worldY -= overlapUp;
            else                            gp.player.worldY += overlapDown;
        }
    }

    @Override
    public void update() {
        setAction();

        // ── Damage player khi chạm ────────────────────────────────────────
        java.awt.Rectangle bossRect = new java.awt.Rectangle(
            worldX + solidAreaDefaultX,
            worldY + solidAreaDefaultY,
            solidArea.width, solidArea.height
        );
        java.awt.Rectangle playerRect = new java.awt.Rectangle(
            gp.player.worldX + gp.player.solidAreaDefaultX,
            gp.player.worldY + gp.player.solidAreaDefaultY,
            gp.player.solidArea.width, gp.player.solidArea.height
        );

        if (bossRect.intersects(playerRect)) {
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

        pushPlayer();

        // ── Di chuyển + kẹp trong vùng ───────────────────────────────────
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

        clampToBound(); // ← giữ boss không bao giờ ra khỏi vùng

        // ── Invincible counter ────────────────────────────────────────────
        if (invicible) {
            invicibleCounter++;
            if (invicibleCounter > 30) {
                invicible = false;
                invicibleCounter = 0;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
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

            // ── Thanh máu ─────────────────────────────────────────────────
            int barW    = gp.tileSize * 3;
            int barH    = 10;
            int barX    = screenX;
            int barY    = screenY - barH - 6;
            int maxLife = 300;
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