package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UI {

    GamePanel gp;
    Font baseFont;
    Graphics2D g2;

    private String currentMessage = "";
    private int messageTimer = 0;
    private static final int MESSAGE_DURATION = 100;

    private String dialogTitle = "";
    private String dialogBody  = "";

    private String proximityObjName = "";
    private String proximityHint    = "";

        // HP bar sprites
    private BufferedImage imgHpBg;
    private BufferedImage imgHpGreen;
    private BufferedImage imgHpYellow;
    private BufferedImage imgHpRed;
    private BufferedImage imgHpFrame;

    // Fill area within the 64x16 sprite (measured from spritesheet)
    // Green fill occupies cols 13-59, rows 7-9  => fillable width = 47px starting at x=13
    private static final int FILL_START_X = 13;   // pixel offset inside sprite where fill begins
    private static final int FILL_MAX_W   = 47;   // max fillable pixels
    private static final int MAX_HP       = 200;

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
        baseFont = Font.createFont(
            Font.TRUETYPE_FONT,
            getClass().getResourceAsStream("/font/FVF Fernando 08.ttf")
        ).deriveFont(Font.PLAIN, 22F);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(baseFont);
    } catch (Exception e) {
        baseFont = new Font("Arial", Font.PLAIN, 22); // fallback nếu load lỗi
    }
        loadHpBarImages();
    }

    private void loadHpBarImages() {
        try {
            imgHpBg     = ImageIO.read(getClass().getResourceAsStream("/hp/hp_bar_bg.png"));
            imgHpGreen  = ImageIO.read(getClass().getResourceAsStream("/hp/hp_fill_green.png"));
            imgHpYellow = ImageIO.read(getClass().getResourceAsStream("/hp/hp_fill_yellow.png"));
            imgHpRed    = ImageIO.read(getClass().getResourceAsStream("/hp/hp_bar_red.png"));
            imgHpFrame  = ImageIO.read(getClass().getResourceAsStream("/hp/hp_bar_frame.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String text) {
        currentMessage = text;
        messageTimer   = MESSAGE_DURATION;
    }

    public void setDialog(String title, String body) {
        dialogTitle = title;
        dialogBody  = body;
    }

    public void setProximityHint(String objName, String hint) {
        proximityObjName = objName;
        proximityHint    = hint;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (gp.gameState == gp.playState || gp.gameState == gp.dialogState) {
            drawHUD();
            drawMessage();
        }

        if (gp.gameState == gp.playState && !proximityHint.isEmpty()) {
            drawProximityHint();
        }

        if (gp.gameState == gp.dialogState) drawDialog();
        if (gp.gameState == gp.pauseState)  drawPauseScreen();
        if (gp.gameState == gp.gameOverState) drawGameOver();
    }

    private void drawGameOver() {
    // Nền mờ đỏ
        g2.setColor(new Color(0f, 0f, 0f, 0.75f));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Chữ GAME OVER
        g2.setFont(baseFont.deriveFont(Font.BOLD, 80F));
        g2.setColor(new Color(220, 30, 30));
        String title = "GAME OVER";
        FontMetrics fm = g2.getFontMetrics();
        int tx = gp.screenWidth / 2 - fm.stringWidth(title) / 2;
        g2.drawString(title, tx, gp.screenHeight / 2 - 20);

        // Gợi ý restart
        g2.setFont(baseFont.deriveFont(Font.PLAIN, 22F));
        g2.setColor(Color.WHITE);
        String sub = "Nhấn R để chơi lại";
        FontMetrics fm2 = g2.getFontMetrics();
        int sx = gp.screenWidth / 2 - fm2.stringWidth(sub) / 2;
        g2.drawString(sub, sx, gp.screenHeight / 2 + 50);
    }

    private void drawHUD() {
        // scale thanh HP
        int scale  = 3;
        int sprW   = 64 * scale;   // 192
        int sprH   = 16 * scale;   // 48
        int margin = 12;

    // ĐỔI THÀNH GÓC TRÊN BÊN TRÁI
        int drawX = margin; 
        int drawY = margin;

        int hp    = Math.max(0, gp.player.HP);
        float ratio = (float) hp / MAX_HP;

        // Background của thanh HP
        if (imgHpBg != null)
            g2.drawImage(imgHpBg, drawX, drawY, sprW, sprH, null);

        // Phần máu trong thanh HP
        // Scale phần fill
        if (ratio > 0f) {
            int fillStartXScaled = FILL_START_X * scale;
            int fillMaxWScaled   = FILL_MAX_W   * scale;
            int fillW            = (int)(fillMaxWScaled * ratio);

            // green > 50%, yellow > 25%, red <= 25%
            // Nháy khi báo đỏ
            BufferedImage fillImg;
            int fillOffsetY;
            if (ratio > 0.5f) {
                fillImg = imgHpGreen;
                fillOffsetY = 6;
            } else if (ratio > 0.25f) {
                fillImg = imgHpYellow;
                fillOffsetY = 6;
            } else {
                boolean flash = (System.currentTimeMillis() / 350) % 2 == 0;
                fillImg = flash ? imgHpRed : null;
                fillOffsetY = 0;
            }

            if (fillImg != null && fillW > 0) {
                // Clip để k bị tràn
                java.awt.Shape oldClip = g2.getClip();
                g2.setClip(drawX + fillStartXScaled, drawY, fillW, sprH);
                g2.drawImage(fillImg, drawX, drawY + fillOffsetY, sprW, sprH, null);
                g2.setClip(oldClip);
            }
        }

        // Khung HP ở trên
        if (imgHpFrame != null)
            g2.drawImage(imgHpFrame, drawX, drawY, sprW, sprH, null);

        // Số HP ở dưới khung
        g2.setFont(baseFont.deriveFont(Font.BOLD, 13F));
        g2.setColor(new Color(255, 220, 220));
        String hpText = hp + " / " + MAX_HP;
        FontMetrics fm = g2.getFontMetrics();
        int textX = drawX + sprW - fm.stringWidth(hpText);
        g2.drawString(hpText, textX, drawY + sprH + 14);
    }

    private void drawMessage() {
        if (messageTimer <= 0) return;
        g2.setFont(baseFont.deriveFont(Font.BOLD, 20F));
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(currentMessage) + 24;
        int x = gp.screenWidth / 2 - w / 2;
        int y = gp.screenHeight - 70;
        float alpha = Math.min(1f, messageTimer / 30f);
        g2.setColor(new Color(0f, 0f, 0f, 0.65f * alpha));
        g2.fillRoundRect(x - 4, y - 26, w + 8, 36, 12, 12);
        g2.setColor(new Color(1f, 1f, 0f, alpha));
        g2.drawString(currentMessage, x + 8, y);
        messageTimer--;
    }

    private void drawProximityHint() {
        String line1 = proximityObjName;
        String line2 = proximityHint;

        g2.setFont(baseFont.deriveFont(Font.BOLD, 16F));
        FontMetrics fm1 = g2.getFontMetrics();
        g2.setFont(baseFont.deriveFont(Font.PLAIN, 14F));
        FontMetrics fm2 = g2.getFontMetrics();

        int w = Math.max(fm1.stringWidth(line1), fm2.stringWidth(line2)) + 28;
        int h = line1.isEmpty() ? 30 : 52;

        int px = gp.player.screenX + gp.tileSize / 2;
        int py = gp.player.screenY - 12;
        int bx = px - w / 2;
        int by = py - h;

        g2.setColor(new Color(10, 10, 30, 210));
        g2.fillRoundRect(bx, by, w, h, 12, 12);
        g2.setColor(new Color(180, 180, 255, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(bx, by, w, h, 12, 12);

        if (!line1.isEmpty()) {
            g2.setFont(baseFont.deriveFont(Font.BOLD, 15F));
            g2.setColor(new Color(255, 220, 100));
            g2.drawString(line1, bx + 14, by + 20);
        }

        g2.setFont(baseFont.deriveFont(Font.PLAIN, 13F));
        g2.setColor(new Color(180, 255, 180));
        int textY = line1.isEmpty() ? by + 20 : by + 38;
        g2.drawString(line2, bx + 14, textY);
    }

    private void drawDialog() {
        int pad  = gp.tileSize;
        int boxX = pad;
        int boxY = (int)(gp.screenHeight * 0.70);
        int boxW = gp.screenWidth - pad * 2;
        int boxH = gp.tileSize * 3 + 10;

        g2.setColor(new Color(10, 10, 30, 215));
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g2.setColor(new Color(160, 160, 255));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        if (!dialogTitle.isEmpty()) {
            g2.setFont(baseFont.deriveFont(Font.BOLD, 20F));
            g2.setColor(new Color(255, 220, 100));
            g2.drawString(dialogTitle, boxX + 20, boxY + 30);
        }

        g2.setFont(baseFont.deriveFont(Font.PLAIN, 19F));
        g2.setColor(Color.WHITE);
        String[] lines = dialogBody.split("\n");
        int lineH = g2.getFontMetrics().getHeight();
        int textY = boxY + (dialogTitle.isEmpty() ? 36 : 56);
        for (String line : lines) {
            g2.drawString(line, boxX + 20, textY);
            textY += lineH;
        }

        g2.setFont(baseFont.deriveFont(Font.ITALIC, 14F));
        g2.setColor(new Color(160, 160, 160));
        g2.drawString("[F] Đóng / Tương tác", boxX + boxW - 160, boxY + boxH - 10);
    }

    public void drawPauseScreen() {
        g2.setFont(baseFont.deriveFont(Font.PLAIN, 80F));
        g2.setColor(Color.WHITE);
        String text = "PAUSED";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, gp.screenWidth / 2 - fm.stringWidth(text) / 2,
                            gp.screenHeight / 2);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}
