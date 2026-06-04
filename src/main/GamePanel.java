package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

    //GAME STATE
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int gameOverState = 4;
    public final int settingState = 5;

    //đối tượng đang tương tác
    public SuperObject currentObject;

    public GamePanel() {
        
        this.setPreferredSize(new Dimension (screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        // XỬ LÝ SỰ KIỆN CLICK CHUỘT
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();

                // 1. Nhánh tương tác khi đang ở màn hình Pause
                if (gameState == pauseState) {
                    if (ui.btnResumeRect.contains(mx, my)) {
                        gameState = playState;
                    }
                    else if (ui.btnSettingRect.contains(mx, my)) {
                        gameState = settingState;
                    }
                    //Nút restart
                    else if (ui.btnRestartRect.contains(mx, my)) {
                        System.out.println("Tắt luồng game Swing và chuyển hướng về MainMenu...");

                        gameThread = null;
                        stopMusic(); // Tắt nhạc nền màn chơi hiện tại

                        // Đóng cửa sổ JFrame chứa GamePanel bằng cơ chế luồng đồ họa Swing an toàn
                        SwingUtilities.invokeLater(() -> {
                            java.awt.Window window = SwingUtilities.getWindowAncestor(GamePanel.this);
                            if (window != null) {
                                window.dispose(); // Giải phóng hoàn toàn JFrame
                            }
                        });

                        //Gọi luồng JavaFX để nạp lại file giao diện MainMenu.fxml
                        javafx.application.Platform.runLater(() -> {
                            try {
                                javafx.stage.Stage mainStage = new javafx.stage.Stage();
                                javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(
                                        getClass().getResource("/view/MainMenu.fxml")
                                );
                                mainStage.setTitle("Harry Potter and the Philosopher's Stone");
                                mainStage.setScene(new javafx.scene.Scene(root, 1024, 768));
                                mainStage.setResizable(false);
                                mainStage.show();
                            } catch (Exception ex) {
                                System.out.println("Lỗi nghiêm trọng khi nạp MainMenu FXML!");
                                ex.printStackTrace();
                            }
                        });
                    }
                }

                //tương tác khi đang ở màn hình Cài đặt (Setting)
                else if (gameState == settingState) {
                    if (ui.btnBackRect.contains(mx, my)) {
                        gameState = pauseState;
                    }
                    if (ui.sliderBounds.contains(mx, my)) {
                        updateVolumeFromMouse(mx);
                    }
                }
            }
        });

        //XỬ LÝ KÉO RÊ CHUỘT ĐỂ ĐIỀU CHỈNH SLIDER VOLUME
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (gameState == settingState) {
                    int mx = e.getX();
                    if (ui.sliderBounds.contains(mx, e.getY())) {
                        updateVolumeFromMouse(mx);
                    }
                }
            }
        });
    }

    private void updateVolumeFromMouse(int mouseX) {
        double minX = ui.sliderBounds.x;
        double percent = (double) (mouseX - minX) / ui.sliderBounds.width;
        if (percent < 0) percent = 0;
        if (percent > 1) percent = 1;

        ui.musicVolume = (int) (percent * 100);
        System.out.println("Volume cập nhật hệ thống: " + ui.musicVolume + "%");

        // Cập nhật âm thanh thực tế nếu cần:
        // music.setVolume(ui.musicVolume);
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
        if (gameState == gameOverState) {
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
            if (gameState == playState) {
                currentObject = null;
            }
        }
        if (gameState == pauseState){

        }
        for (Entity m : monster) {
            if (m != null) m.update();
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
