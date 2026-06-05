package object;
import entity.ProjectTile;
import main.*;

public class OBJ_FireBall extends ProjectTile {
    private static final int MAX_RANGE = 60;
    private int startX, startY;
    private boolean started = false;
    private float vx = 0, vy = 0;

    public OBJ_FireBall(GamePanel gp) {
        super(gp);
        name    = "Fireball";
        speed   = 5;
        maxLife = 80;
        life    = maxLife;
        attack  = 20;
        useCost = 1;
        alive   = false;
        solidArea         = new java.awt.Rectangle(0, 0, gp.tileSize * 2, gp.tileSize * 2);
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;

        getImage();
    }

    @Override
    public void set(int worldX, int worldY, String direction, boolean alive, entity.Entity user) {
        super.set(worldX, worldY, direction, alive, user);
        this.startX = worldX;
        this.startY = worldY;
        this.started = true;

        float diag = speed / (float) Math.sqrt(2);
        switch (direction) {
            case "up":         vx =     0;  vy = -speed; break;
            case "down":       vx =     0;  vy =  speed; break;
            case "left":       vx = -speed; vy =      0; break;
            case "right":      vx =  speed; vy =      0; break;
            case "up-left":    vx =  -diag; vy =  -diag; break;
            case "up-right":   vx =   diag; vy =  -diag; break;
            case "down-left":  vx =  -diag; vy =   diag; break;
            case "down-right": vx =   diag; vy =   diag; break;
            default:           vx =     0;  vy =  speed; break;
        }
    }

    @Override
    public void update() {
        // 1. Check tầm bắn
        if (started) {
            int dx       = Math.abs(worldX - startX);
            int dy       = Math.abs(worldY - startY);
            int distPixel = Math.max(dx, dy);
            if (distPixel >= MAX_RANGE * gp.tileSize) {
                alive = false;
                return;
            }
        }

        // 2. Nếu boss bắn → check va chạm với player
        if (user != gp.player) {
            java.awt.Rectangle fireRect = new java.awt.Rectangle(
                worldX + solidAreaDefaultX,
                worldY + solidAreaDefaultY,
                solidArea.width,
                solidArea.height
            );
            java.awt.Rectangle playerRect = new java.awt.Rectangle(
                gp.player.worldX + gp.player.solidAreaDefaultX,
                gp.player.worldY + gp.player.solidAreaDefaultY,
                gp.player.solidArea.width,
                gp.player.solidArea.height
            );

            if (fireRect.intersects(playerRect) && !gp.player.invicible) {
                gp.player.HP          -= attack;
                if (gp.player.HP < 0) gp.player.HP = 0;
                gp.player.invicible        = true;
                gp.player.invicibleCounter = 0;
                gp.ui.showMessage("Fireball! -" + attack + " HP");
                gp.playSE(1);
                alive = false;
                if (gp.player.HP <= 0) gp.gameState = gp.gameOverState;
                return;
            }
            // 3. Di chuyển theo vx/vy (hỗ trợ hướng chéo)
            worldX += (int) vx;
            worldY += (int) vy;
            life--;
            if (life <= 0) alive = false;
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
        else {
            // Player bắn → dùng super (4 hướng thẳng)
            super.update();
        }

    }

    public void getImage() {
        up1 = up2 = down1 = down2 = left1 = left2 = right1 = right2
            = setup("/skill/Fire_for_boss_3.png");
    }

    @Override
    public void draw(java.awt.Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        java.awt.image.BufferedImage image = (spriteNum == 1) ? down1 : down2;

        if (image != null)
            g2.drawImage(image, screenX, screenY, gp.tileSize * 2, gp.tileSize * 2, null);
    }
}