package object;
import entity.ProjectTile;
import main.*;
import java.awt.image.BufferedImage;

public class OBJ_Ulti extends ProjectTile {
    private static final int MAX_RANGE = 20;

    public static final int COOLDOWN_FRAMES = 300;
    public static int cooldownTimer = 0;

    // Cache ảnh tĩnh — load 1 lần duy nhất, mọi instance dùng chung
    private static BufferedImage cachedUp1, cachedUp2;
    private static BufferedImage cachedDown1, cachedDown2;
    private static BufferedImage cachedLeft1, cachedLeft2;
    private static BufferedImage cachedRight1, cachedRight2;
    private static boolean imagesLoaded = false;

    public OBJ_Ulti(GamePanel gp) {
        super(gp);
        name = "Ulti";
        speed = 8;
        maxLife = 999;
        life = maxLife;
        attack = 500;
        useCost = 1;
        alive = false;
        // Chỉ load ảnh 1 lần duy nhất trong suốt game
        if (!imagesLoaded) {
            cachedUp1    = setup("/skill/Ulti_1.png");
            cachedUp2    = setup("/skill/Ulti_2.png");
            cachedDown1  = setup("/skill/Ulti_1.png");
            cachedDown2  = setup("/skill/Ulti_3.png");
            cachedLeft1  = setup("/skill/Ulti_1.png");
            cachedLeft2  = setup("/skill/Ulti_2.png");
            cachedRight1 = setup("/skill/Ulti_1.png");
            cachedRight2 = setup("/skill/Ulti_3.png");
            imagesLoaded = true;
        }
        // Gán từ cache — không tốn I/O
        up1 = cachedUp1; up2 = cachedUp2;
        down1 = cachedDown1; down2 = cachedDown2;
        left1 = cachedLeft1; left2 = cachedLeft2;
        right1 = cachedRight1; right2 = cachedRight2;
    }

    public static void tickCooldown() {
        if (cooldownTimer > 0) cooldownTimer--;
    }

    public static boolean isReady() {
        return cooldownTimer <= 0;
    }

    public static void triggerCooldown() {
        cooldownTimer = COOLDOWN_FRAMES;
    }

    @Override
    public void update() {
        if (!alive) return;
        if (started) {
            int dist = Math.max(
                Math.abs(worldX - startX),
                Math.abs(worldY - startY)
            );
            if (dist >= MAX_RANGE * gp.tileSize) {
                alive = false;
                return;
            }
        }
        super.update();
    }

    // getImage() không cần nữa nhưng giữ lại để tránh lỗi nếu có nơi gọi
    public void getImage() { /* no-op: dùng static cache trong constructor */ }
}
