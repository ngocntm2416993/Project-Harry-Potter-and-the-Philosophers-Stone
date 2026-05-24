package entity;

import main.KeyHandler;
import main.UtilityTool;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int HP;
    public int normalSpeed;
    public long speedBoostEndTime = 0;

    public Player (GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        
        getPlayerImage();
    }

    // Vi tri bat dau cua nhan vat
    public void setDefaultValues() {
        worldX = 15 * gp.tileSize; 
        worldY = 15 * gp.tileSize;
        normalSpeed = 4;
        speed = normalSpeed;
        direction = "down";
        HP = 200;
    }

    public void getPlayerImage() {
        up1 = setup("/player/boy_up_1.png");
        up2 = setup("/player/boy_up_2.png");
        down1 = setup("/player/boy_down_1.png");
        down2 = setup("/player/boy_down_2.png");
        left1 = setup("/player/boy_left_1.png");
        left2 = setup("/player/boy_left_2.png");
        right1 = setup("/player/boy_right_1.png");
        right2 = setup("/player/boy_right_2.png");
    }

    public BufferedImage setup (String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }
    public void update() {
        if (keyH.upPressed == true) {
            direction = "up";
        }
        else if (keyH.downPressed == true) {
            direction = "down";
        }
        else if (keyH.rightPressed == true) {
            direction = "right";
        }
        else if (keyH.leftPressed == true) {
            direction = "left";
        }

        if (keyH.downPressed == true || keyH.upPressed == true ||
            keyH.leftPressed == true || keyH.rightPressed == true) {
            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile (this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            // IF COLLISION IF FALSE, PLAYER CAN MOVE
            if (collisionOn == false) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":            
                        worldY += speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                }
            }
                spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1){
                    spriteNum = 2;
                }
                else if (spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
        if (speedBoostEndTime > 0 && System.currentTimeMillis() > speedBoostEndTime) {
            speed = normalSpeed;
            speedBoostEndTime = 0;
        }
    }

    public void pickUpObject(int i) {
        /* if ( i != -1) {
            String objectName = gp.obj[i].name;

            switch (objectName){
                case "HP":
                    HP += 50;
                    gp.playSE(1);
                    break;
                case "Speed":
                    gp.playSE(1);
                    speed = normalSpeed + 10;
                    speedBoostEndTime = System.currentTimeMillis() + 5000;
                    break;
                case "Door":
                    gp.playSE(2);
                    break;

            }
            gp.obj[i]= null; 
        }*/
    }

    
    public void draw(Graphics2D g2) {
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

        // ĐỔI DÒNG NÀY THÀNH screenX, screenY (Không dùng worldX, worldY ở đây)
        g2.drawImage(image, screenX, screenY,  null);
    }
}
