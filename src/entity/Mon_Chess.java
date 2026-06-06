package entity;

import main.GamePanel;
import main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.awt.Graphics2D;

public class Mon_Chess extends Entity{
    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 1000; // 1 giây

    int startX, startY; // vị trí ban đầu
    int moveRange = 3;  // di chuyển tối đa 3 tile tính từ vị trí ban đầu
    Random random = new Random();
    int boundLeft;
    int boundRight;
    int startDelay;
    int nextTurn;
    String imagePath;

    public Mon_Chess(GamePanel gp, int startCol, int startRow, int boundLeftCol, int boundRightCol, String imagePath) {
        super(gp);
        this.imagePath = imagePath; // nhận ảnh từ bên ngoài
        this.worldX = gp.tileSize * startCol;
        this.worldY = gp.tileSize * startRow;
        this.boundLeft  = gp.tileSize * boundLeftCol;
        this.boundRight = gp.tileSize * boundRightCol;
        this.startX = worldX;
        this.startY = worldY;
//        //direction = "down";
//        direction = "right"; // bắt đầu đi sang phải
//        speed = 1;
        Random random = new Random();
        this.worldX = boundLeft + random.nextInt(boundRight - boundLeft);
        // 50% đi phải, 50% đi trái
        this.direction = random.nextBoolean() ? "right" : "left";


        // this.speed = random.nextInt(2) + 1; // tốc độ 1 hoặc 2
        this.speed = 7;                      // cố định tốc độ 3
        this.speed = random.nextInt(2) + 2;  // random 2 hoặc 3
        this.speed = random.nextInt(3) + 2;  // random 2, 3 hoặc 4


        nextTurn = 60 + random.nextInt(180); // quay đầu sau 60~240 frame đầu tiên
        getImage();
        // solidArea = new java.awt.Rectangle(8, 8, gp.tileSize - 16, gp.tileSize - 16);
        // solidAreaDefaultX = solidArea.x;
        // solidAreaDefaultY = solidArea.y;
        int inset = 8; // thử 8 thay vì 16
        solidArea = new java.awt.Rectangle(inset, inset, gp.tileSize * 2 - inset * 2, gp.tileSize * 2 - inset * 2);
        solidAreaDefaultX = inset;
        solidAreaDefaultY = inset;
    }

    public void getImage() {
        up1 = up2 = down1 = down2 = setupLarge(imagePath);
        left1 = left2 = right1 = right2 = setupLarge(imagePath);
    }

        public void setAction() {
            // Vẫn giữ chặn biên cứng để không bao giờ ra ngoài
            if (worldX >= boundRight) { direction = "left";  actionLockCounter = 0; nextTurn = 60 + random.nextInt(180); }
            if (worldX <= boundLeft)  { direction = "right"; actionLockCounter = 0; nextTurn = 60 + random.nextInt(180); }

            // Quay đầu ngẫu nhiên giữa đường
            actionLockCounter++;
            if (actionLockCounter >= nextTurn) {
                direction = direction.equals("right") ? "left" : "right"; // đổi hướng
                actionLockCounter = 0;
                nextTurn = 60 + random.nextInt(180); // đặt lại thời gian quay tiếp theo
            }
        }

    @Override
    public void update() {
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);

        // Chỉ dùng checkPlayer để chặn di chuyển (collision physics),
        // KHÔNG gây damage ở đây — damage xử lý tập trung trong Player.checkNPCContact()
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
    public BufferedImage setupLarge(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize * 2, gp.tileSize * 2); // x2 ngay khi load
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize * 2 > gp.player.worldX - gp.player.screenX &&
            worldX < gp.player.worldX + gp.player.screenX + gp.tileSize &&
            worldY + gp.tileSize * 2 > gp.player.worldY - gp.player.screenY &&
            worldY < gp.player.worldY + gp.player.screenY + gp.tileSize) {

            BufferedImage image;
            switch (direction) {
                case "left":  image = (spriteNum == 1) ? left1  : left2;  break;
                case "right": image = (spriteNum == 1) ? right1 : right2; break;
                case "up":    image = (spriteNum == 1) ? up1    : up2;    break;
                default:      image = (spriteNum == 1) ? down1  : down2;  break;
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize * 2, gp.tileSize * 2, null);
        }
    }
    
}
