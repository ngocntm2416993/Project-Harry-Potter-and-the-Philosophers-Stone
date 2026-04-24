package main;
//***
import java.awt.*;

public class Player {
    int x, y;

    public Player(GamePanel x, KeyHandler y) {

    }

    public void draw(Graphics2D g2, int tileSize) {
        g2.fillRect(x, y, tileSize, tileSize);
    }

    public void update() {
    }
}
