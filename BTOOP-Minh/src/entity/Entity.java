package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Entity {
    public int moveRange;
    GamePanel gp;
    public int worldX, worldY;
    public int speed;
    public int actionLockCounter = 0;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public Entity(GamePanel gp){
        this.gp=gp;
    }
    public void setAction () {
        actionLockCounter++;

        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(4); // 0, 1, 2, 3

            switch (i) {
                case 0: direction = "up";    break;
                case 1: direction = "down";  break;
                case 2: direction = "left";  break;
                case 3: direction = "right"; break;
            }
            actionLockCounter = 0;
        }
    }
    public void update () {
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);           // ✅ check tường
        gp.cChecker.checkPlayer(this);         // ✅ check va chạm player
        //gp.cChecker.checkEntity(this, gp.npc); // ← thêm dòng này
        //gp.cChecker.checkObject(this,false);
        //gp.cChecker.checkPlayer(this);

        // IF COLLISION IS FALSE, PLAYER CAN MOVE
        if(collisionOn != false){  // ✅ hoặc viết gọn: if(!collisionOn)
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
        spriteCounter++;
        if (spriteCounter > 12){
            if (spriteNum ==1){
                spriteNum = 2;
            }
            else if (spriteNum ==2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    public void draw(Graphics2D g2){
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX > gp.player.worldX - gp.player.screenX - gp.tileSize &&
                worldX < gp.player.worldX + gp.player.screenX + gp.tileSize &&
                worldY > gp.player.worldY - gp.player.screenY  - gp.tileSize &&
                worldY < gp.player.worldY + gp.player.screenY + gp.tileSize) {
            BufferedImage image = null;
            switch (direction) {
                case "up":
                    if (spriteNum == 1) image = up1;
                    if (spriteNum == 2) image = up2;
                    break;
                case "down":
                    if (spriteNum == 1) image = down1;
                    if (spriteNum == 2) image = down2;
                    break;
                case "left":
                    if (spriteNum == 1) image = left1;
                    if (spriteNum == 2) image = left2;
                    break;
                case "right":
                    if (spriteNum == 1) image = right1;
                    if (spriteNum == 2) image = right2;
                    break;
            }
            g2.drawImage (image, screenX, screenY,gp.tileSize, gp.tileSize, null);
        }
    }
    public BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }
}
