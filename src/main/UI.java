package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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

    public UI(GamePanel gp) {
        this.gp = gp;
        baseFont = new Font("Arial", Font.PLAIN, 22);
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
        g2.setFont(baseFont.deriveFont(Font.PLAIN, 20F));
        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(10, 8, 140, 58, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawString("HP : " + gp.player.HP, 20, 32);
        g2.drawString("Map: " + gp.currentMap, 20, 56);
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
