package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class ProjectTile extends Entity{
    protected Entity user;
    public boolean started = false;
    public int startX, startY;
    public int maxLife;
    public int attack;
    public int useCost;
    public int defense;
    public ProjectTile( GamePanel gp){
        super(gp);
        solidArea = new java.awt.Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
    }
    public void set(int worldX, int worldY, String direction, boolean alive,Entity user ){
        this.worldX = worldX;
        this.worldY= worldY;
        this.direction = direction;
        this.alive = alive;
        this.user= user;
        this.life = this.maxLife;
        this.startX=worldX;
        this.startY=worldY;
        this.started=true;
    }
    public void update(){
        if( user == gp.player){
            int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
            if(monsterIndex !=-1 ){
                gp.player.damageMonster ( monsterIndex, attack);
                alive=false;

            }
        }

        switch(direction) {
            case "up": worldY -= speed; break; 
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }
        life--;
        if(life<=0){
            alive=false;
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0; // reset counter, KHÔNG reset spriteNum về 0
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image = null;
        switch (direction) {
            case "up":    image = (spriteNum == 1) ? up1 : up2; break;
            case "down":  image = (spriteNum == 1) ? down1 : down2; break;
            case "left":  image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }

        if (image != null) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
    
}

