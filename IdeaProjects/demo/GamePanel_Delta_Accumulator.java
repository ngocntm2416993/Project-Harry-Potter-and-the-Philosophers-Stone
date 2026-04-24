package main;

import java.awt.*;

import javax.swing.JPanel;

public class GamePanel_Delta_Accumulator extends JPanel implements Runnable {
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    int FPS = 60;
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    // set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel_Delta_Accumulator() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

//***        ArrayList<Player> players = new ArrayList<>();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        // phan check hoat dong 2 bien timer - dem count
        long timer =0;
        int drawCount =0;

        while (gameThread != null){
            currentTime = System.nanoTime();
            delta += ( currentTime - lastTime) / drawInterval;

            //check hoat dong
            timer += ( currentTime - lastTime);
            lastTime = currentTime;
            if( delta>= 1){
                update();
                repaint();
                delta --;
                // check hoat dong
                drawCount++;
            }
            if( timer>=1000000000){
                System.out.println("FPS : "+ drawCount);
                drawCount =0;
                timer =0;
            }
        }
    }

    public void update() {
        if (keyH.upPressed == true) {
            playerY -= playerSpeed;
        } else if (keyH.downPressed == true) {
            playerY += playerSpeed;
        } else if (keyH.leftPressed == true) {
            playerX -= playerSpeed;
        } else if (keyH.rightPressed == true) {
            playerX += playerSpeed;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize, tileSize); // kich thuoc nhan vat
        g2.dispose(); // giai phong

    }
}
