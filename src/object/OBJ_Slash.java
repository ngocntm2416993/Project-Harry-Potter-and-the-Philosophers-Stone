package object;
import entity.ProjectTile;
import main.*;

public class OBJ_Slash extends ProjectTile {
    private static final int MAX_RANGE = 3; // tile
    private int startX, startY;
    private boolean started = false;

    public OBJ_Slash(GamePanel gp) {
        super(gp);
        name = "Slash";          
        speed = 5;
        maxLife = 80;
        life = maxLife;
        attack = 20;              
        useCost = 1;
        alive = false;
        getImage();           
    }

    public void set(int worldX, int worldY, String direction, boolean alive, entity.Entity user) {
        super.set(worldX, worldY, direction, alive, user);
        // Ghi lại vị trí bắt đầu mỗi lần bắn
        this.startX  = worldX;
        this.startY  = worldY;
        this.started = true;
    }

    @Override
    public void update() {
        // Check khoảng cách đã đi
        if (started) {
            int dx = Math.abs(worldX - startX);
            int dy = Math.abs(worldY - startY);
            int distPixel = Math.max(dx, dy); // dùng max vì chỉ đi 1 trục
            int maxPixel  = MAX_RANGE * gp.tileSize; // 3 * 48 = 144px

            if (distPixel >= maxPixel) {
                alive = false;
                return;
            }
        }

        // Giữ nguyên logic gốc
        super.update();
    }

    public void getImage() {   
        up1    = setup("/skill/Slash_up(1).png");
        up2    = setup("/skill/Slash_up(2).png");
        down1  = setup("/skill/Slash_down(1).png");
        down2  = setup("/skill/Slash_down(2).png");
        left1  = setup("/skill/Slash_left(1).png");
        left2  = setup("/skill/Slash_left(2).png");
        right1 = setup("/skill/Slash_right(1).png");
        right2 = setup("/skill/Slash_right(2).png");
        
    }
}