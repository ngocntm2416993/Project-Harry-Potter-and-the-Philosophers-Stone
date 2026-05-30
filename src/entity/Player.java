package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int HP;
    public int normalSpeed;
    public long speedBoostEndTime = 0;

    private boolean interactConsumed = false;
    private boolean justClosedDialog = false;
    private int activeDialogObjIndex = -1;
    private int nearbyObjIndex = -1;

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
    }

    public void setDefaultValues() {
        worldX      = 28 * gp.tileSize;
        worldY      = 27 * gp.tileSize;
        normalSpeed = 4;
        speed       = normalSpeed;
        direction   = "down";
        HP          = 200;
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


    public void update() {
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

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up":    image = (spriteNum == 1) ? up1    : up2;    break;
            case "down":  image = (spriteNum == 1) ? down1  : down2;  break;
            case "left":  image = (spriteNum == 1) ? left1  : left2;  break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }
        g2.drawImage(image, screenX, screenY, null);
    }
}