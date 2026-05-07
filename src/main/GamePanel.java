package main;

import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;  // 48 x 48 tile
    public final int maxScreenCol = 24;
    public final int maxScreenRow = 16;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    //WORLD SETTING
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow; 

    //FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    public AssetSetter aSetter = new AssetSetter(this);
    
    public int currentMap = 0; // 0 là map 1


    public GamePanel() {
        
        this.setPreferredSize(new Dimension (screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    // mặc định là map 1
    public void setUpGame() {
        aSetter.setMap1();
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta  = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta --;
            }
        }
    }
    public void update() {
        player.update();
    }
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        // TILE
        tileM.draw (g2);

        // OBJECT
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) obj[i].draw(g2, this);
        }

        // PLAYER
        player.draw(g2);

        g2.dispose();
    }
    public void switchMap(int mapIndex) {
        currentMap = mapIndex;
        if (currentMap == 0) {
        tileM.loadMap("/maps/map_01.txt");
        aSetter.setMap1();
        player.worldX = 20 * tileSize;
        player.worldY = 21 * tileSize;
    } 
    else if (currentMap == 1) {
        tileM.loadMap("/maps/map_02.txt");
        aSetter.setMap2();
        player.worldX = 5 * tileSize;
        player.worldY = 10 * tileSize;
    }
    }
}
