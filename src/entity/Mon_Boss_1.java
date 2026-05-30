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

    public Mon_Boss_1(GamePanel gp, int col, int row) {
        super(gp);
        this.worldX = col * gp.tileSize;
        this.worldY = row * gp.tileSize;
        this.direction = "down";
        this.speed = 0; // đứng yên

        // solidArea khớp 3x3 tile
        solidArea = new Rectangle(0, 0, gp.tileSize * 3, gp.tileSize * 3);
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;

        getImage();
    }

    public void getImage() {
        left1    = setupBoss("/monster/Boss_1/Left(1)");
        left2    = setupBoss("/monster/Boss_1/Left(2)");
        right1  = setupBoss("/monster/Boss_1/Right(1)");
        right2  = setupBoss("/monster/Boss_1/Right(2)");
        up1    = setupBoss("/monster/Boss_1/Left(1)");
        up2    = setupBoss("/monster/Boss_1/Left(2)");
        down1  = setupBoss("/monster/Boss_1/Right(1)");
        down2  = setupBoss("/monster/Boss_1/Right(2)");
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
        // đứng yên, không làm gì
    }

    @Override
    public void update() {
        // int tdCX = (trapdoorCol + 1) * gp.tileSize; // center trapdoor
        // int tdCY = (trapdoorRow + 1) * gp.tileSize;

        // int dx = gp.player.worldX - tdCX;
        // int dy = gp.player.worldY - tdCY;

        // if (Math.abs(dx) > Math.abs(dy)) {
        //     if (dx > 0) {
        //         direction = "right";
        //         targetX = (trapdoorCol + 4) * gp.tileSize; // (15, 11)
        //         targetY = trapdoorRow * gp.tileSize;
        //     } else {
        //         direction = "left";
        //         targetX = (trapdoorCol - 4) * gp.tileSize; // (9, 11)
        //         targetY = trapdoorRow * gp.tileSize;
        //     }
        // } else {
        //     if (dy > 0) {
        //         direction = "down";
        //         targetX = trapdoorCol * gp.tileSize;        // (12, 14)
        //         targetY = (trapdoorRow + 4) * gp.tileSize;
        //     } else {
        //         direction = "up";
        //         targetX = trapdoorCol * gp.tileSize;        // (12, 8)
        //         targetY = (trapdoorRow - 4) * gp.tileSize;
        //     }
        // }

        // // Di chuyển boss về target
        // int moveSpeed = 3;
        // if (Math.abs(worldX - targetX) > moveSpeed)
        //     worldX += (worldX < targetX) ? moveSpeed : -moveSpeed;
        // else
        //     worldX = targetX;

        // if (Math.abs(worldY - targetY) > moveSpeed)
        //     worldY += (worldY < targetY) ? moveSpeed : -moveSpeed;
        // else
        //     worldY = targetY;

        // spriteCounter++;
        // if (spriteCounter > 12) {
        //     spriteNum = (spriteNum == 1) ? 2 : 1;
        //     spriteCounter = 0;
        // }
        int tdCX = (trapdoorCol + 1) * gp.tileSize;
        int tdCY = (trapdoorRow + 1) * gp.tileSize;

        int dx = gp.player.worldX - tdCX;
        int dy = gp.player.worldY - tdCY;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                direction = "right";
                targetX = (trapdoorCol + 4) * gp.tileSize;
                targetY = trapdoorRow * gp.tileSize;
            } else {
                direction = "left";
                targetX = (trapdoorCol - 4) * gp.tileSize;
                targetY = trapdoorRow * gp.tileSize;
            }
        } else {
            if (dy > 0) {
                direction = "down";
                targetX = trapdoorCol * gp.tileSize;
                targetY = (trapdoorRow + 4) * gp.tileSize;
            } else {
                direction = "up";
                targetX = trapdoorCol * gp.tileSize;
                targetY = (trapdoorRow - 4) * gp.tileSize;
            }
        }

        speed = 4;

        // Di chuyển X
        if (Math.abs(worldX - targetX) > speed) {
            direction = (worldX < targetX) ? "right" : "left";
            collisionOn = false;
            gp.cChecker.checkTile(this); // check cả tile lẫn object layer
            if (!collisionOn)
                worldX += (worldX < targetX) ? speed : -speed;
        } else {
            worldX = targetX;
        }

        // Di chuyển Y
        if (Math.abs(worldY - targetY) > speed) {
            direction = (worldY < targetY) ? "down" : "up";
            collisionOn = false;
            gp.cChecker.checkTile(this);
            if (!collisionOn)
                worldY += (worldY < targetY) ? speed : -speed;
        } else {
            worldY = targetY;
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
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
        }
    }
}