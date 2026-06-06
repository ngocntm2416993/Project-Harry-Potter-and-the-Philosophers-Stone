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
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UI {

    GamePanel gp;
    Font baseFont;
    Graphics2D g2;

    private String centerMessage = "";
    private int centerMessageTimer = 0;
    private static final int CENTER_MESSAGE_DURATION = 180; // 3s


    private String currentMessage = "";
    private int messageTimer = 0;
    private static final int MESSAGE_DURATION = 100;

    private String dialogTitle = "";
    private String dialogBody = "";

    private String proximityObjName = "";
    private String proximityHint = "";

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


    private int endgameAlpha = 0;
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
    public Rectangle sliderSEBounds = new Rectangle();

    // Biến lưu giá trị âm lượng thực tế của game (0 -> 100)
    public int musicVolume = 50;
    public int seVolume = 50;

    public int puzzleSelection = 0;
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
        loadPauseImages();
        loadSettingImages(); // Khởi tạo nạp ảnh setting khi tạo UI
        baseFont = new Font("Arial", Font.PLAIN, 22);  //chỗ này mới này 
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
            imgBtnBack = ImageIO.read(getClass().getResourceAsStream("/view/back.png"));
        } catch (IOException e) {
            System.out.println("Không load được ảnh tài nguyên Setting: " + e.getMessage());
        }
    }
    
    public void showCenterMessage(String text) {
        centerMessage      = text;
        centerMessageTimer = CENTER_MESSAGE_DURATION;
    }

    private void drawCenterMessage() {
        if (centerMessageTimer <= 0) return;

            g2.setFont(baseFont.deriveFont(Font.BOLD, 36F));
            FontMetrics fm = g2.getFontMetrics();
            int w = fm.stringWidth(centerMessage) + 40;
            int h = 60;
            int x = gp.screenWidth  / 2 - w / 2;
            int y = gp.screenHeight / 2 - h / 2;

            float alpha = Math.min(1f, centerMessageTimer / 60f); // fade out 1s cuối
            g2.setColor(new Color(0f, 0f, 0f, 0.7f * alpha));
            g2.fillRoundRect(x, y, w, h, 16, 16);

            g2.setFont(baseFont.deriveFont(Font.BOLD, 36F));
            g2.setColor(new Color(1f, 0.85f, 0f, alpha));
            g2.drawString(centerMessage,
                gp.screenWidth  / 2 - fm.stringWidth(centerMessage) / 2,
                y + h / 2 + fm.getAscent() / 2 - 4);

            centerMessageTimer--;
    }

    public void showMessage(String text) {
        currentMessage = text;
        messageTimer   = MESSAGE_DURATION;
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
            drawCenterMessage();
        }

        if (gp.gameState == gp.playState && !proximityHint.isEmpty()) {
            drawProximityHint();
        }

        if (gp.gameState == gp.dialogState) drawDialog();
        if (gp.gameState == gp.pauseState) drawPauseMenu();
        if (gp.gameState == gp.settingState) drawSettingMenu();
        if (gp.gameState == gp.gameOverState) drawGameOver();
        if (gp.gameState == gp.endGameState) drawEndgameScreen();
        if (gp.gameState == gp.puzzleState && gp.currentObject instanceof object.OBJ_BookTable) {
            drawPuzzleMenu(((object.OBJ_BookTable) gp.currentObject).choices);
        }
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

        int bgW = 800;
        int bgH = 600;
        int bgX = (sw - bgW) / 2;
        int bgY = (sh - bgH) / 2;
        if (imgBgSetting != null) {
            g2.drawImage(imgBgSetting, bgX, bgY, bgW, bgH, null);
        }

        int sliderW = 350;
        int sliderH = 40;
        int iconW = 50;
        int iconH = 50;

        // --- SLIDER 1: MUSIC (Đẩy cao lên) ---
        int musicSliderX = bgX + 288;
        int musicSliderY = bgY + 195; // Giảm xuống 195 (gốc trước đó là 240) để đẩy cao lên
        sliderBounds.setBounds(musicSliderX, musicSliderY, sliderW, sliderH);

        // Vẽ thanh trống Music
        if (imgBar != null) g2.drawImage(imgBar, musicSliderX, musicSliderY, sliderW, sliderH, null);

        // Vẽ thanh đầy Music
        if (imgBarFull != null) {
            double percentMusic = musicVolume / 100.0;
            int drawWidthMusic = (int) (sliderW * percentMusic);
            if (drawWidthMusic > 0) {
                Shape oldClip = g2.getClip();
                g2.setClip(musicSliderX, musicSliderY, drawWidthMusic, sliderH);
                g2.drawImage(imgBarFull, musicSliderX, musicSliderY, sliderW, sliderH, null);
                g2.setClip(oldClip);
            }
        }
        // Vẽ Icon nút kéo Music
        int iconMusicX = musicSliderX + (int) (sliderW * (musicVolume / 100.0)) - (iconW / 2);
        int iconMusicY = musicSliderY + (sliderH - iconH) / 2;
        if (imgIcon != null) g2.drawImage(imgIcon, iconMusicX, iconMusicY, iconW, iconH, null);


        // --- SLIDER 2: SOUND EFFECT (Đẩy cao lên) ---
        int seSliderX = bgX + 288;
        int seSliderY = bgY + 315; // Giảm xuống 315 (gốc trước đó là 360) để đẩy cao lên
        sliderSEBounds.setBounds(seSliderX, seSliderY, sliderW, sliderH);

        // Vẽ thanh trống SE
        if (imgBar != null) g2.drawImage(imgBar, seSliderX, seSliderY, sliderW, sliderH, null);

        // Vẽ thanh đầy SE
        if (imgBarFull != null) {
            double percentSE = seVolume / 100.0;
            int drawWidthSE = (int) (sliderW * percentSE);
            if (drawWidthSE > 0) {
                Shape oldClip = g2.getClip();
                g2.setClip(seSliderX, seSliderY, drawWidthSE, sliderH);
                g2.drawImage(imgBarFull, seSliderX, seSliderY, sliderW, sliderH, null);
                g2.setClip(oldClip);
            }
        }
        // Vẽ Icon nút kéo SE
        int iconSEX = seSliderX + (int) (sliderW * (seVolume / 100.0)) - (iconW / 2);
        int iconSEY = seSliderY + (sliderH - iconH) / 2;
        if (imgIcon != null) g2.drawImage(imgIcon, iconSEX, iconSEY, iconW, iconH, null);


        // --- NÚT BACK (Đẩy cao lên) ---
        int btnW = 250;
        int btnH = 60;
        int btnX = bgX + 275;
        int btnY = bgY + 435; // Giảm xuống 435 (gốc trước đó là 480) để đẩy cao lên
        if (imgBtnBack != null) {
            g2.drawImage(imgBtnBack, btnX, btnY, btnW, btnH, null);
        }
        btnBackRect.setBounds(btnX, btnY, btnW, btnH);
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
        int textY = boxY + (dialogTitle.isEmpty() ? 36 : 65);
        for (String line : lines) {
            g2.drawString(line, boxX + 20, textY);
            textY += lineH;
        }

        g2.setFont(baseFont.deriveFont(Font.ITALIC, 14F));
        g2.setColor(new Color(160, 160, 160));
        g2.drawString("[F] Đóng / Tương tác", boxX + boxW - 200, boxY + boxH - 10);
    }

    public void drawPauseScreen() {
        g2.setFont(baseFont.deriveFont(Font.PLAIN, 80F));
        g2.setColor(Color.WHITE);
        String text = "PAUSED";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, gp.screenWidth / 2 - fm.stringWidth(text) / 2,
                            gp.screenHeight / 2);
    }

    public void drawEndgameScreen() {
        // 1. Tăng dần giá trị alpha (tối đa 255)
        if (endgameAlpha < 255) {
            endgameAlpha += 5; // Tốc độ hiện dần (càng lớn càng nhanh)
        }

        // 2. Phủ mờ nền (alpha dựa trên endgameAlpha)
        g2.setColor(new Color(0, 0, 0, Math.min(endgameAlpha / 2, 150)));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // 3. Thiết lập khung và nội dung (dùng alpha cho màu sắc)
        int boxW = 500;
        int boxH = 260;
        int boxX = (gp.screenWidth - boxW) / 2;
        int boxY = (gp.screenHeight - boxH) / 2;

        // Sử dụng endgameAlpha để làm cho toàn bộ bảng "hiện dần"
        int alpha = Math.min(endgameAlpha, 255);

        // Nền hộp thoại
        g2.setColor(new Color(10, 10, 30, (int)(alpha * 0.9)));
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        // Viền hộp thoại
        g2.setColor(new Color(160, 160, 255, alpha));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        // 4. Vẽ chữ (cũng cần áp dụng alpha để mờ dần hiện ra)
        g2.setFont(baseFont.deriveFont(Font.BOLD, 20F));
        g2.setColor(new Color(255, 220, 100, alpha));
        String title = "SẢN PHẦM CỦA NHÓM 3";
        g2.drawString(title, boxX + 20, boxY + 45);

        g2.setFont(baseFont.deriveFont(Font.PLAIN, 22F));
        g2.setColor(new Color(255, 255, 255, alpha));
        g2.drawString("Cảm ơn bạn đã trải nghiệm game.", boxX + 20, boxY + 90);
        g2.setColor(new Color(180, 255, 180, alpha));
        g2.drawString("CẢM ƠN ANH ĐỘ MIXI", boxX + 20, boxY + 150);

        g2.setFont(baseFont.deriveFont(Font.ITALIC, 14F));
        g2.setColor(new Color(160, 160, 160, alpha));
        g2.drawString("[ENTER] Quay lại Main menu", boxX + boxW - 250, boxY + boxH - 15);
    }
    public void resetEndgameAlpha() {
        this.endgameAlpha = 0;
    }

    public void drawPuzzleMenu(String[] choices) {
        int boxX = gp.tileSize * 2;
        int boxY = gp.tileSize * 2;
        int boxW = gp.screenWidth - (gp.tileSize * 4);
        int boxH = gp.tileSize * 8;

        g2.setColor(new Color(10, 10, 30, 230));
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g2.setColor(new Color(160, 160, 255));
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        g2.setFont(baseFont.deriveFont(Font.BOLD, 22F));
        g2.setColor(Color.WHITE);
        g2.drawString("Chọn một chai thuốc:", boxX + 20, boxY + 40);

        for (int i = 0; i < choices.length; i++) {
            g2.setFont(baseFont.deriveFont(Font.PLAIN, 20F));
            if (i == puzzleSelection) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> " + choices[i], boxX + 40, boxY + 80 + (i * 40));
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString(choices[i], boxX + 40, boxY + 80 + (i * 40));
            }
        }
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}
