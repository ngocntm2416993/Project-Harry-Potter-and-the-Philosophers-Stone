package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UI {
    
    GamePanel gp;
    Font arial_40;
    BufferedImage image;

    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
    }

    public void draw(Graphics2D g2) {
        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);
        g2.drawString("HP: " + gp.player.HP, 25, 50);
        g2.drawString("Speed: " + gp.player.speed, 25, 98);
    }
}
