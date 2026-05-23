package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class NPC_OldMan extends Entity{
    int startX, startY; // vị trí ban đầu
    int moveRange = 3;  // di chuyển tối đa 3 tile tính từ vị trí ban đầu
    Random random = new Random();
    public NPC_OldMan(GamePanel gp, int startCol, int startRow) {
        super(gp);
        this.worldX = gp.tileSize * startCol;
        this.worldY = gp.tileSize * startRow;
        this.startX = worldX;
        this.startY = worldY;
        direction = "down";
        speed = 1;
        getImage();
    }
    public void getImage() {

        up1 = setup("/npc/oldman_up_1");
        up2 = setup("/npc/oldman_up_2");
        down1 = setup("/npc/oldman_down_1");
        down2 = setup("/npc/oldman_down_2");
        left1 = setup("/npc/oldman_left_1");
        left2 = setup("/npc/oldman_left_2");
        right1 = setup("/npc/oldman_right_1");
        right2 = setup("/npc/oldman_right_2");
    }
    final int boundLeft   = 3  * gp.tileSize;
    final int boundRight  = 27 * gp.tileSize;
    final int boundTop    = 3  * gp.tileSize;
    final int boundBottom = 27 * gp.tileSize;
    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter == 120) {
            int i = random.nextInt(4);

            int nextX = worldX;
            int nextY = worldY;

            switch (i) {
                case 0: nextY -= gp.tileSize; break; // up
                case 1: nextY += gp.tileSize; break; // down
                case 2: nextX -= gp.tileSize; break; // left
                case 3: nextX += gp.tileSize; break; // right
            }

            // Chỉ đổi hướng nếu vị trí mới còn trong HCN
            boolean withinBound = nextX >= boundLeft  &&
                    nextX <= boundRight  &&
                    nextY >= boundTop    &&
                    nextY <= boundBottom;

            if (withinBound) {
                switch (i) {
                    case 0: direction = "up";    break;
                    case 1: direction = "down";  break;
                    case 2: direction = "left";  break;
                    case 3: direction = "right"; break;
                }
            } else {
                // Quay về hướng spawn nếu ra ngoài vùng
                if (worldX < boundLeft)   direction = "right";
                if (worldX > boundRight)  direction = "left";
                if (worldY < boundTop)    direction = "down";
                if (worldY > boundBottom) direction = "up";
            }
            actionLockCounter = 0;
//            if (i <= 25) {
//                direction = "up";
//            } else if (i <= 50) {
//                direction = "down";
//            } else if (i <= 75) {
//                direction = "left";
//            } else {
//                direction = "right";
//            }

            actionLockCounter = 0;
        }
    }

    @Override
    public void update() {
        setAction();
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
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }
}
