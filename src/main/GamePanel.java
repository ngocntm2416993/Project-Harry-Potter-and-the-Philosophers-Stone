package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;  // 48 x 48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTING
    public final int maxWorldCol = 30;
    public final int maxWorldRow = 30;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow; 
    public int currentMap = 1;

    // FPS
    int FPS = 60;

    // SYSTEM
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public MapTransition mapTransition = new MapTransition(this);

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    public Entity npc[] = new Entity[50];
    public Entity monster[] = new Entity[10];
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectTileList = new ArrayList<>();

    //GAME STATE
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int gameOverState = 4;

    //đối tượng đang tương tác
    public SuperObject currentObject;

    public GamePanel() {
        
        this.setPreferredSize(new Dimension (screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

        public void setUpGame() {
            aSetter.setObject();
            aSetter.setNPC(); // ← thêm dòng này
            playMusic(0);
            stopMusic();
            gameState = playState;
        }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0/FPS;
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
        if (gameState == gameOverState ) {
            if (keyH.restartPressed) {
                int spawnX = AssetSetter.MAP_SPAWN[1][0]; // index 1 = map2
                int spawnY = AssetSetter.MAP_SPAWN[1][1];
                player.worldX = spawnX * tileSize;
                player.worldY = spawnY * tileSize;
                player.HP = 200;
                player.speed = player.normalSpeed;
                aSetter.setNPC();
                gameState = playState;
            }
            return;
        }
        if (gameState == playState || gameState == dialogState ){
            //PLAYER
            player.update();

            //MAPS
            mapTransition.update();

            // NPC 
            for (Entity n : npc){
                if (n != null) n.update();
            }

            if (gameState == pauseState){

            }
            for (Entity m : monster) {
                if (m != null) m.update();
            }

            for(int i=0;i<projectTileList.size();i++){
                if(projectTileList.get(i)!=null){
                    if(projectTileList.get(i).alive==true){
                        projectTileList.get(i).update();
                    }
                    if(projectTileList.get(i).alive==false){
                        projectTileList.remove(i);
                    }
                }
            }
            
        }
        
        if (gameState == playState) {
            currentObject = null;
        }
    }
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // TILE
        tileM.draw(g2);

        // OBJECT
        for (SuperObject o : obj) {
            if (o != null) o.draw(g2, this);
        }

        // NPC
        for (Entity n : npc) {
            if (n != null) n.draw(g2);
        }

        //MONSTER
        for (Entity m : monster) {
            if (m != null) m.draw(g2);
        }

        //ProjectTile
        for (Entity m : projectTileList) {
            if (m != null) m.draw(g2);
        }

        // PLAYER
        player.draw(g2);

        // UI
        ui.draw(g2);

        // MAP TRANSITION
        mapTransition.draw(g2);

        g2.dispose();
    }

    public void changeMap(int mapId) {
        mapTransition.startTransition(mapId);
    }

    public void playMusic (int i) {
        
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }


}
