package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Slash;
import object.OBJ_Ulti;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int HP;
    public int normalSpeed;
    public long speedBoostEndTime = 0;

    public boolean isCompleteGame = true;
    private boolean interactConsumed = false;
    private boolean justClosedDialog = false;
    private int activeDialogObjIndex = -1;
    private int nearbyObjIndex = -1;
    // ── Unlock flags ──────────────────────────────────────────────────────
    public boolean hasSlash = false;      // nhặt lọ xanh dương mới dùng được
    public boolean hasFireball = false;
    public boolean hasUlti = false;
    private boolean ultiConsumed = false;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth  / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        int inset = 6;
        solidArea = new Rectangle(inset, inset + 4,
                gp.tileSize - inset * 2,
                gp.tileSize - inset * 2 - 4);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    public void setDefaultValues() {
        worldX      = 28 * gp.tileSize;
        worldY      = 27 * gp.tileSize;
        normalSpeed = 4;
        speed       = normalSpeed;
        direction   = "down";
        HP          = 200;
        attackArea.width= 36;
        attackArea.height=26;
        life = maxLife;
        projectTile = new OBJ_Slash(gp);

    }

    public void setPosition (int tileX, int tileY){
        worldX = tileX * gp.tileSize;
        worldY = tileY * gp.tileSize;
    }

    public void getPlayerImage() {
        up1    = setup("/player/boy_up_1.png");
        up2    = setup("/player/boy_up_2.png");
        down1  = setup("/player/boy_down_1.png");
        down2  = setup("/player/boy_down_2.png");
        left1  = setup("/player/boy_left_1.png");
        left2  = setup("/player/boy_left_2.png");
        right1 = setup("/player/boy_right_1.png");
        right2 = setup("/player/boy_right_2.png");
    }

    public void getPlayerAttackImage() {
        attackUpl    = setup("/player/attack_up_left.png");
        attackUp2    = setup("/player/attack_up_right.png");
        attackDown1  = setup("/player/attack_down_left_1.png");
        attackDown2  = setup("/player/attack_down_right_1.png");
        attackLeft1  = setup("/player/attack_left.png");
        attackLeft2  = setup("/player/no_attack_left.png");
        attackRight1 = setup("/player/attack_right.png");
        attackRight2 = setup("/player/prepare_right_1.png");
    }


    public void update() {
        if(attacking==true){
            attacking();
        }

        boolean moving = keyH.upPressed || keyH.downPressed ||
                         keyH.leftPressed || keyH.rightPressed;

        if (keyH.upPressed)         direction = "up";
        else if (keyH.downPressed)  direction = "down";
        else if (keyH.rightPressed) direction = "right";
        else if (keyH.leftPressed)  direction = "left";

        checkProximity();

        if (!keyH.interactPressed) {
            interactConsumed = false;
            justClosedDialog = false;
        }

        if (keyH.interactPressed && !interactConsumed && !justClosedDialog) {
            interactConsumed = true;
            if (gp.gameState == gp.playState) {
                handleInteraction();
            } else if (gp.gameState == gp.dialogState) {
                if (activeDialogObjIndex != -1 && gp.obj[activeDialogObjIndex] != null) {
                    gp.obj[activeDialogObjIndex].onInteract(gp, this);
                } else {
                    closeDialog();
                }
            }
        }

        if (gp.gameState == gp.dialogState) {
            checkDialogProximity();
        }

        if (moving) {

            collisionOn = false;
            gp.cChecker.checkTile(this);
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            if (gp.currentMap == 2) gp.cChecker.checkEntity(this, gp.npc);

            if (!collisionOn) {
                switch (direction) {
                    case "up":    worldY -= speed; break;
                    case "down":  worldY += speed; break;
                    case "right": worldX += speed; break;
                    case "left":  worldX -= speed; break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }

        if (speedBoostEndTime > 0 && System.currentTimeMillis() > speedBoostEndTime) {
            speed = normalSpeed;
            speedBoostEndTime = 0;
        }

        // Đếm invincible frame
        if (invicible) {
            invicibleCounter++;
            if (invicibleCounter > 60) { // 1 giây nhấp nháy
                invicible = false;
                invicibleCounter = 0;
            }
        }

        // if (keyH.slashPressed && !projectTile.alive) {
        //     projectTile = new OBJ_Slash(gp);  // tạo mới mỗi lần bắn
        //     projectTile.set(worldX, worldY, direction, true, this);
        //     gp.projectTileList.add(projectTile);
        // }
        if (keyH.slashPressed) {
            if (!hasSlash) {
                gp.ui.showMessage("Cần lọ thuốc xanh dương để dùng chiêu Slash!");
            } else if (!projectTile.alive) {
                projectTile = new OBJ_Slash(gp);
                projectTile.set(worldX, worldY, direction, true, this);
                gp.projectTileList.add(projectTile);
            }
        }

        if (keyH.UltiPressed) {
            if (!hasUlti) {
                gp.ui.showMessage("Cần lọ thuốc tím để dùng chiêu này!");
            } else {
                useUlti();
            }
        }

        checkNPCContact();
    }

    // private void useUlti() {
    //     if (ultiConsumed) return; // chặn giữ phím
    //     ultiConsumed = true;

    //     String[] dirs = {"up", "down", "left", "right"};
    //     for (String dir : dirs) {
    //         entity.ProjectTile pt = new object.OBJ_Slash(gp);
    //         pt.set(worldX, worldY, dir, true, this);
    //         gp.projectTileList.add(pt);
    //     }
    //     gp.ui.showMessage("Ulti!");
    //     gp.playSE(1);
    // }

    private void useUlti() {
        if (!OBJ_Ulti.isReady()) {
            int sec = OBJ_Ulti.cooldownTimer / 60 + 1;
            gp.ui.showMessage("Ulti hồi chiêu: còn " + sec + "s");
            return;
        }

        String[] dirs = {"up", "down", "left", "right"};
        for (String dir : dirs) {
            OBJ_Ulti pt = new OBJ_Ulti(gp);
            pt.set(worldX, worldY, dir, true, this);
            gp.projectTileList.add(pt);
        }

        OBJ_Ulti.triggerCooldown(); // ← bắt đầu đếm cooldown
        gp.ui.showMessage("Ulti!");
        gp.playSE();
    }

    public void attacking() {
        spriteCounter++; 
        if(spriteCounter <= 5){
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) { 
            spriteNum = 2; 
            // Save the current worldX, worldY, solidArea 
            int currentWorldX = worldX; 
            int currentWorldY = worldY; 
            int solidAreaWidth = solidArea.width; 
            int solidAreaHeight = solidArea.height; 
            // Adiust plaver's worldX/Y for the attackArea 
            switch (direction) { 
                case "up": worldY -= attackArea.height; break; 
                case "down": worldY += attackArea.height; break; 
                case "left": worldX -= attackArea.width; break; 
                case "right": worldX += attackArea. width; break; 
            }            // attackArea becomes solidArea 
            solidArea.width = attackArea.width; 
            solidArea.height = attackArea.height; 
            //Check monster r collision with the updated worldX, worldY and solidArea 
            attack =20;
            int monsterIndex = gp. cChecker.checkEntity(this, gp.monster) ;
            damageMonster (monsterIndex,attack);

            // After checking collision, resotre the original data 
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth; 
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > 25) { 
            spriteNum = 1; 
            spriteCounter = 0 ;
            attacking = false;
        }
    }


    public void interactNPC (int i) {
        if (keyH.interactPressed==true){
            if (i != -1) { 
                    gp.gameState = gp.dialogState; 
                    gp.npc[i].setAction(); 
            }
            else attacking = true;
        }
    }

    private void pushPlayerFromBoss() {
        if(gp.monster[0]==null) return;
        entity.Entity boss = gp.monster[0];
        java.awt.Rectangle playerRect = new java.awt.Rectangle(
            worldX + solidAreaDefaultX,
            worldY + solidAreaDefaultY,
            solidArea.width, solidArea.height
        );
        java.awt.Rectangle bossRect = new java.awt.Rectangle(
            boss.worldX + boss.solidAreaDefaultX,
            boss.worldY + boss.solidAreaDefaultY,
            boss.solidArea.width, boss.solidArea.height
        );

        if (!playerRect.intersects(bossRect)) return;

        // Tính overlap theo từng trục, đẩy ra theo trục nhỏ hơn
        int overlapLeft  = (playerRect.x + playerRect.width)  - bossRect.x;
        int overlapRight = (bossRect.x  + bossRect.width)     - playerRect.x;
        int overlapUp    = (playerRect.y + playerRect.height)  - bossRect.y;
        int overlapDown  = (bossRect.y  + bossRect.height)     - playerRect.y;

        int minX = Math.min(overlapLeft, overlapRight);
        int minY = Math.min(overlapUp,   overlapDown);

        if (minX < minY) {
            // Đẩy theo trục X
            if (overlapLeft < overlapRight) worldX -= overlapLeft;
            else                            worldX += overlapRight;
        } else {
            // Đẩy theo trục Y
            if (overlapUp < overlapDown) worldY -= overlapUp;
            else                         worldY += overlapDown;
        }
    }

    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 1000; // 1 giây

    private void checkNPCContact() {
        if (gp.currentMap == 2){
            int expand = speed + 4; // mở rộng thêm theo hướng di chuyển
            int px = worldX + solidAreaDefaultX;
            int py = worldY + solidAreaDefaultY;
            int pw = solidArea.width;
            int ph = solidArea.height;

            // Mở rộng rect về phía đang di chuyển
            switch (direction) {
                case "up":    py -= expand; ph += expand; break;
                case "down":  ph += expand;               break;
                case "left":  px -= expand; pw += expand; break;
                case "right": pw += expand;               break;
            }

            java.awt.Rectangle playerRect = new java.awt.Rectangle(px, py, pw, ph);

            for (int i = 0; i < gp.npc.length; i++) {
                if (gp.npc[i] == null) continue;
                java.awt.Rectangle npcRect = new java.awt.Rectangle(
                    gp.npc[i].worldX + gp.npc[i].solidAreaDefaultX,
                    gp.npc[i].worldY + gp.npc[i].solidAreaDefaultY,
                    gp.npc[i].solidArea.width,
                    gp.npc[i].solidArea.height
                );

                if (playerRect.intersects(npcRect)) {
                    long now = System.currentTimeMillis();
                    if (now - lastDamageTime > DAMAGE_COOLDOWN) {
                        lastDamageTime = now;
                        HP -= 10;
                        //player nhấp nháy
                        invicible = true;
                        invicibleCounter = 0;
                        gp.ui.showMessage("Bị quân cờ tấn công! -10 HP");
                        gp.playSE();
                        if (HP <= 0) {
                            HP = 0;
                            gp.gameState = gp.gameOverState;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void checkProximity() {
        if (gp.gameState == gp.dialogState) return;

        int playerCX = worldX + solidAreaDefaultX + solidArea.width  / 2;
        int playerCY = worldY + solidAreaDefaultY + solidArea.height / 2;

        nearbyObjIndex = -1;
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) continue;
            if (!gp.obj[i].hasProximityHint()) continue;

            int objCX = gp.obj[i].worldX + gp.obj[i].solidAreaDefaultX + gp.obj[i].solidArea.width  / 2;
            int objCY = gp.obj[i].worldY + gp.obj[i].solidAreaDefaultY + gp.obj[i].solidArea.height / 2;

            int dx = Math.abs(playerCX - objCX);
            int dy = Math.abs(playerCY - objCY);

            int range = gp.obj[i].proximityRange
                      + gp.obj[i].solidArea.width  / 2
                      + gp.obj[i].solidArea.height / 2;
            range = Math.min(range, gp.tileSize * 4);

            if (dx < range && dy < range) {
                nearbyObjIndex = i;
                break;
            }
        }

        if (nearbyObjIndex >= 0) {
            gp.ui.setProximityHint(
                gp.obj[nearbyObjIndex].name,
                gp.obj[nearbyObjIndex].getProximityHint()
            );
        } else {
            gp.ui.setProximityHint("", "");
        }
    }

    private void handleInteraction() {
        int objIdx = (nearbyObjIndex != -1)
                ? nearbyObjIndex
                : gp.cChecker.checkObjectInteraction(this);

        if (objIdx != -1 && gp.obj[objIdx] != null) {
            activeDialogObjIndex = objIdx;
            gp.obj[objIdx].onInteract(gp, this);
        } else {
            activeDialogObjIndex = -1;
            attacking = true;  // ← THÊM DÒNG NÀY
        }
    }

    private void checkDialogProximity() {
        if (activeDialogObjIndex < 0 || activeDialogObjIndex >= gp.obj.length) {
            closeDialog(); return;
        }
        if (gp.obj[activeDialogObjIndex] == null || !isNearObject(activeDialogObjIndex)) {
            closeDialog();
        }
    }

    private void closeDialog() {
        gp.gameState = gp.playState;
        activeDialogObjIndex = -1;
        justClosedDialog = true;
        gp.ui.setDialog("", "");
    }
    private boolean isNearObject(int objIdx) {
        if (gp.obj[objIdx] == null) return false;
        int dx = Math.abs(worldX - gp.obj[objIdx].worldX);
        int dy = Math.abs(worldY - gp.obj[objIdx].worldY);
        return dx < gp.tileSize * 5 && dy < gp.tileSize * 5;
    }

    public void pickUpObject(int i) {
        if (i == -1) return;
        if (gp.obj[i] != null) gp.obj[i].onContact(gp, this);
    }

    public void damageMonster(int i, int attack) {
        if (i == -1) return;
        if (gp.monster[i] == null) return;

        if (!gp.monster[i].invicible) {
            try { gp.playSE(); } catch (Exception e) {} // ← dùng index an toàn
            int damage = attack - gp.monster[i].defense;
            if (damage < 0) damage = 0;
            gp.monster[i].life -= damage;
            gp.ui.showMessage(damage + " damage!");
            gp.monster[i].invicible = true;
            if (gp.monster[i].life <= 0) {
                gp.monster[i] = null;
                gp.ui.showMessage("Boss defeated!");
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (invicible && (invicibleCounter / 5) % 2 == 1) return;
        BufferedImage image = null;
        switch (direction) {
            case "up":    
                if(attacking == false ){
                    image = (spriteNum == 1) ? up1    : up2;
                }
                if(attacking==true){
                    image = (spriteNum == 1) ? attackUpl    : attackUp2;
                }    
            break;
            case "down":
                if(attacking == false ){
                    image = (spriteNum == 1) ? down1    : down2;
                }
                if(attacking==true){
                    image = (spriteNum == 1) ? attackDown1    : attackDown2;
                }  
            break;  
            case "left":
                if(attacking == false ){
                    image = (spriteNum == 1) ? left1    : left2;
                }
                if(attacking==true){
                    image = (spriteNum == 1) ? attackLeft1    : attackLeft2;
                }  
            break; 
            case "right": 
                if(attacking == false ){
                    image = (spriteNum == 1) ? right1    : right2;
                }
                if(attacking==true){
                    image = (spriteNum == 1) ? attackRight1    : attackRight2;
                }  
            break; 
        }
        g2.drawImage(image, screenX, screenY, null);
    }
}