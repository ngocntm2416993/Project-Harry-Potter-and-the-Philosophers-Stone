package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

    protected GamePanel gp;

    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUpl, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public String direction;
    public int actionLockCounter=0;
    public boolean alive;
    public int attack;  
    public String name = ""; 

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean attacking = false;
    public Rectangle attackArea = new Rectangle(0,0,48,48);

    public boolean invicible = false;
    public int invicibleCounter = 0;
    public ProjectTile projectTile;

    public int life;
    public int maxLife;
    public int mana;
    public int maxMana;
    
    //public ProjectTile projectTile;
    public int useCost;

    public int defense=0;

    public Entity (GamePanel gp){
        this.gp = gp;
    }

    public void setAction(){

    }
    
    public void update(){
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkPlayer(this);

        if (!collisionOn && speed > 0) {
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
    
    public void draw (Graphics2D g2){
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

            g2.drawImage (image, screenX, screenY, null);
        }
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
}
