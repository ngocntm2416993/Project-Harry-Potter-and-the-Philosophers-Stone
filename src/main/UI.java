package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
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
    private String dialogBody = "";

    private String proximityObjName = "";
    private String proximityHint = "";

    // Ảnh pause menu
    private BufferedImage imgBgPause, imgPaused, imgSetting, imgResume, imgRestart;

    // Tài nguyên hình ảnh cho màn hình Setting bằng Swing Overlay
    private BufferedImage imgBgSetting, imgBar, imgBarFull, imgIcon, imgBtnBack;

    // Vùng click của từng nút khi ở màn hình Pause
    public Rectangle btnSettingRect = new Rectangle();
    public Rectangle btnResumeRect = new Rectangle();
    public Rectangle btnRestartRect = new Rectangle();

    // Vùng click cho nút Back và thanh trượt Slider khi ở màn hình Setting
    public Rectangle btnBackRect = new Rectangle();
    public Rectangle sliderBounds = new Rectangle();

    // Biến lưu giá trị âm lượng thực tế của game (0 -> 100)
    public int musicVolume = 50;

    public UI(GamePanel gp) {
        this.gp = gp;
        baseFont = new Font("Arial", Font.PLAIN, 22);
        loadPauseImages();
        loadSettingImages(); // Khởi tạo nạp ảnh setting khi tạo UI
    }

    private void loadPauseImages() {
        try {
            imgBgPause = ImageIO.read(getClass().getResourceAsStream("/view/bgpause.png"));
            imgPaused = ImageIO.read(getClass().getResourceAsStream("/view/paused.png"));
            imgSetting = ImageIO.read(getClass().getResourceAsStream("/view/1.png"));
            imgResume = ImageIO.read(getClass().getResourceAsStream("/view/resume.png"));
            imgRestart = ImageIO.read(getClass().getResourceAsStream("/view/restart.png"));
        } catch (IOException e) {
            System.out.println("Không load được ảnh pause menu: " + e.getMessage());
        }
    }

    private void loadSettingImages() {
        try {
            imgBgSetting = ImageIO.read(getClass().getResourceAsStream("/view/bgsetting.png"));
            imgBar = ImageIO.read(getClass().getResourceAsStream("/view/bar.png"));
            imgBarFull = ImageIO.read(getClass().getResourceAsStream("/view/barfull.png"));
            imgIcon = ImageIO.read(getClass().getResourceAsStream("/view/icon.png"));
            imgBtnBack = ImageIO.read(getClass().getResourceAsStream("/view/Start.png"));
        } catch (IOException e) {
            System.out.println("Không load được ảnh tài nguyên Setting: " + e.getMessage());
        }
    }

    public void showMessage(String text) {
        currentMessage = text;
        messageTimer = MESSAGE_DURATION;
    }

    public void setDialog(String title, String body) {
        dialogTitle = title;
        dialogBody = body;
    }

    public void setProximityHint(String objName, String hint) {
        proximityObjName = objName;
        proximityHint = hint;
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
        if (gp.gameState == gp.pauseState) drawPauseMenu();
        if (gp.gameState == gp.settingState) drawSettingMenu();
        if (gp.gameState == gp.gameOverState) drawGameOver();
    }

    private void drawPauseMenu() {
        int sw = gp.screenWidth;
        int sh = gp.screenHeight;

        if (imgBgPause != null) {
            g2.drawImage(imgBgPause, 0, 0, sw, sh, null);
        } else {
            g2.setColor(new Color(0f, 0f, 0f, 0.7f));
            g2.fillRect(0, 0, sw, sh);
        }

        int btnW = 265;
        int btnH = 55;
        int titleW = 300;
        int titleH = 180;
        int spacing = 12;
        int centerX = sw / 2;

        int totalH = titleH + spacing + btnH * 3 + spacing * 2;
        int startY = (sh - totalH) / 2;

        int titleX = centerX - titleW / 2;
        if (imgPaused != null) {
            g2.drawImage(imgPaused, titleX, startY, titleW, titleH, null);
        }

        int btnX = centerX - btnW / 2;
        int currentY = startY + titleH + spacing;

        if (imgSetting != null) g2.drawImage(imgSetting, btnX, currentY, btnW, btnH, null);
        btnSettingRect.setBounds(btnX, currentY, btnW, btnH);
        currentY += btnH + spacing;

        if (imgResume != null) g2.drawImage(imgResume, btnX, currentY, btnW, btnH, null);
        btnResumeRect.setBounds(btnX, currentY, btnW, btnH);
        currentY += btnH + spacing;

        if (imgRestart != null) g2.drawImage(imgRestart, btnX, currentY, btnW, btnH, null);
        btnRestartRect.setBounds(btnX, currentY, btnW, btnH);
    }

    private void drawSettingMenu() {
        int sw = gp.screenWidth;
        int sh = gp.screenHeight;

        if (imgBgPause != null) {
            g2.drawImage(imgBgPause, 0, 0, sw, sh, null);
        }

        //Vẽ khung bảng điều khiển lớn ở trung tâm (800x600)
        int bgW = 800;
        int bgH = 600;
        int bgX = (sw - bgW) / 2;
        int bgY = (sh - bgH) / 2;
        if (imgBgSetting != null) {
            g2.drawImage(imgBgSetting, bgX, bgY, bgW, bgH, null);
        }

        //Thiết kế vẽ hệ thống Slider custom
        int sliderX = bgX + 288;
        int sliderY = bgY + 266;
        int sliderW = 350;
        int sliderH = 40;
        sliderBounds.setBounds(sliderX, sliderY, sliderW, sliderH);

        //Vẽ thanh nền trống
        if (imgBar != null) {
            g2.drawImage(imgBar, sliderX, sliderY, sliderW, sliderH, null);
        }

        //Cắt và vẽ thanh trạng thái đã kéo đầy
        if (imgBarFull != null) {
            double percent = musicVolume / 100.0;
            int drawWidth = (int) (sliderW * percent);

            if (drawWidth > 0) {
                Shape oldClip = g2.getClip(); // Lưu lại clip cũ
                g2.setClip(sliderX, sliderY, drawWidth, sliderH);
                g2.drawImage(imgBarFull, sliderX, sliderY, sliderW, sliderH, null);
                g2.setClip(oldClip); // Khôi phục clip cũ hệ thống an toàn
            }
        }

        //Vẽ con trỏ icon hình nốt nhạc vàng chuyển động
        int iconW = 50;
        int iconH = 50;
        double percent = musicVolume / 100.0;
        int iconX = sliderX + (int) (sliderW * percent) - (iconW / 2);
        int iconY = sliderY + (sliderH - iconH) / 2;
        if (imgIcon != null) {
            g2.drawImage(imgIcon, iconX, iconY, iconW, iconH, null);
        }

        int btnW = 250;
        int btnH = 60;
        int btnX = bgX + 275;
        int btnY = bgY + 416;
        if (imgBtnBack != null) {
            g2.drawImage(imgBtnBack, btnX, btnY, btnW, btnH, null);
        }
        btnBackRect.setBounds(btnX, btnY, btnW, btnH);
    }

    private void drawGameOver() {
        g2.setColor(new Color(0f, 0f, 0f, 0.75f));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setFont(baseFont.deriveFont(Font.BOLD, 80F));
        g2.setColor(new Color(220, 30, 30));
        String title = "GAME OVER";
        FontMetrics fm = g2.getFontMetrics();
        int tx = gp.screenWidth / 2 - fm.stringWidth(title) / 2;
        g2.drawString(title, tx, gp.screenHeight / 2 - 20);
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
        int pad = gp.tileSize;
        int boxX = pad;
        int boxY = (int) (gp.screenHeight * 0.70);
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

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}