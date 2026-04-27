package main;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import entity.Player;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    int FPS = 60;
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);


    // set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    public  void run(){
        double drawInterval = 1000000000/FPS; // 0,01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;
        while(gameThread != null){
            // ktra currentTime
//            long currentTime = System.nanoTime();
//            System.out.println("currentTime is: "+ currentTime);
//            long currentTime2 = System.currentTimeMillis();
//            System.out.println("currentTimeMillis is: "+currentTime2);

            // 1. update information - character position

            update();
            // 2. draw screen with update information
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if ( remainingTime <0) remainingTime = 0;
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                //throw new RuntimeException(e);
                e.printStackTrace();
            }
        }
    }
    public void update(){
        player.update();
    }
    public void paintComponent ( Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        player.draw(g2);
        g2.dispose(); // giai phong
    }
}