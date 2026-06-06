package object;
import entity.ProjectTile;
import main.*;
public class OBJ_Ulti extends ProjectTile {
     // Bay hết màn hình: screenWidth / tileSize = 16 tile
    private static final int MAX_RANGE = 20; // tile, dư ra 4 tile ngoài viền

    // Cooldown: 5 giây = 300 frame (60fps)
    public static final int COOLDOWN_FRAMES = 300;
    public static int cooldownTimer = 0; // static → dùng chung mọi instance

    public OBJ_Ulti(GamePanel gp) {
        super(gp);
        name = "Ulti";          
        speed = 8;
        maxLife = 999;
        life = maxLife;
        attack = 500;              
        useCost = 1;
        alive = false;
        getImage();           
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

    // public void set(int worldX, int worldY, String direction, boolean alive, entity.Entity user) {
    //     super.set(worldX, worldY, direction, alive, user);
    // }

    @Override
    public void update() {
        if (!alive) return;
        // Check khoảng cách đã đi
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

    public void getImage() {   
        up1    = setup("/skill/Ulti_1.png");
        up2    = setup("/skill/Ulti_2.png");
        down1  = setup("/skill/Ulti_1.png");
        down2  = setup("/skill/Ulti_3.png");
        left1  = setup("/skill/Ulti_1.png");
        left2  = setup("/skill/Ulti_2.png");
        right1 = setup("/skill/Ulti_1.png");
        right2 = setup("/skill/Ulti_3.png");
        
    }
}