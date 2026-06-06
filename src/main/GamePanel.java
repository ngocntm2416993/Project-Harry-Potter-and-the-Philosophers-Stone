package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import entity.Entity;
import entity.Player;
import object.OBJ_Ulti;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
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
    public Sound music = new Sound();
    public Sound se = new Sound();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public MapTransition mapTransition = new MapTransition(this);

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[50];
    public Entity npc[] = new Entity[50];
    public Entity monster[] = new Entity[10];
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectTileList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int gameOverState = 4;
    public final int settingState = 5;
    public final int endGameState = 6;
    //đối tượng đang tương tác
    public SuperObject currentObject;

    public final int puzzleState = 7;
    public boolean isDoorUnlocked = false;
    public GamePanel gp;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
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
                // Trong đoạn addMouseListener -> mousePressed:
                else if (gameState == settingState) {
                    if (ui.btnBackRect.contains(mx, my)) {
                        gameState = pauseState;
                    }
                    // Check click thanh Music
                    else if (ui.sliderBounds.contains(mx, my)) {
                        updateVolumeFromMouse(mx);
                    }
                    // Check click thanh Sound Effect (THÊM MỚI NHÁNH NÀY)
                    else if (ui.sliderSEBounds.contains(mx, my)) {
                        updateSEVolumeFromMouse(mx);
                    }
                }


            }
        });

        //XỬ LÝ KÉO RÊ CHUỘT ĐỂ ĐIỀU CHỈNH SLIDER VOLUME
        // Trong đoạn addMouseMotionListener -> mouseDragged:
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (gameState == settingState) {
                    int mx = e.getX();
                    int my = e.getY();
                    // Kéo rê thanh Music
                    if (ui.sliderBounds.contains(mx, my)) {
                        updateVolumeFromMouse(mx);
                    }
                    // Kéo rê thanh Sound Effect (THÊM MỚI NHÁNH NÀY)
                    else if (ui.sliderSEBounds.contains(mx, my)) {
                        updateSEVolumeFromMouse(mx);
                    }
                }
            }
        });
    }

    private void updateVolumeFromMouse(int mouseX) { // Đây là Music Volume
        double minX = ui.sliderBounds.x;
        double percent = (double) (mouseX - minX) / ui.sliderBounds.width;
        if (percent < 0) percent = 0;
        if (percent > 1) percent = 1;

        ui.musicVolume = (int) (percent * 100);
        System.out.println("Music Volume: " + ui.musicVolume + "%");
        music.setVolume(ui.musicVolume);
    }

    private void updateSEVolumeFromMouse(int mouseX) { // THÊM MỚI HÀM NÀY CHO SE
        double minX = ui.sliderSEBounds.x;
        double percent = (double) (mouseX - minX) / ui.sliderSEBounds.width;
        if (percent < 0) percent = 0;
        if (percent > 1) percent = 1;

        ui.seVolume = (int) (percent * 100);
        System.out.println("Sound Effect Volume: " + ui.seVolume + "%");
        se.setVolume(ui.seVolume);
    }

    private void loadSounds() {
        music.soundURL[0] = getClass().getResource("/sound/man1.wav");
        music.soundURL[1] = getClass().getResource("/sound/man2.wav");
        music.soundURL[2] = getClass().getResource("/sound/man3.wav");
        music.soundURL[3] = getClass().getResource("/sound/man4.wav");

        se.soundURL[0] = getClass().getResource("/sound/power_up.wav");
        // Preload tất cả SE vào cache — chỉ chạy 1 lần khi khởi động
        se.preloadAll();
    }

    public void setUpGame() {
        aSetter.setObject();
        aSetter.setNPC();
        loadSounds();
        music.setVolume(ui.musicVolume); // 50 mặc định
        se.setVolume(ui.seVolume);//50 mặc định
        playMusic(0);
        gameState = playState;
        GamePanelHolder.instance = this;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
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
                    System.out.println("currentMap = " + currentMap);
                    int spawnX = AssetSetter.MAP_SPAWN_player[currentMap-1][0];
                    int spawnY = AssetSetter.MAP_SPAWN_player[currentMap-1][1];
                    System.out.println( spawnX);
                    System.out.println( spawnY);
                    
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


        OBJ_Ulti.tickCooldown();
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

    public void playMusic(int i) {
        try {
            music.setFile(i);
            music.setVolume(ui.musicVolume);
            music.play();
            music.loop();
        } catch (Exception e) {
            System.err.println("Lỗi playMusic index " + i + ": " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (music.clip != null) {
            music.clip.stop();
            music.clip.close(); //giải phóng tài nguyên
        }
    }

    public void playSE() {
        // Dùng clip đã cache sẵn — không load lại, không block game thread
        se.playCached(0);
    }

    public void changeMusic(int mapId) {
        stopMusic();
        playMusic(mapId);
    }

    public void backToMainMenu() {
        // 1. Dừng luồng game để tránh lỗi xung đột bộ nhớ
        gameThread = null;
        stopMusic();

        // 2. Đóng cửa sổ Game Swing hiện tại
        SwingUtilities.invokeLater(() -> {
            java.awt.Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });

        // 3. Khởi chạy lại cửa sổ JavaFX MainMenu
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
                System.err.println("Lỗi khi tải MainMenu FXML:");
                ex.printStackTrace();
            }
        });
    }

    public void resetGame() {
        this.currentMap = 1;

        // Nạp lại map 1 - nó sẽ tự reset toàn bộ dữ liệu map cũ
        tileM.loadMap("/maps/map1.txt");

        // Reset trạng thái player
        player.setDefaultValues();
        player.hasSlash = false;
        player.hasUlti = false;
        isDoorUnlocked = false;

        // Dọn dẹp các list đối tượng động
        projectTileList.clear();
        Arrays.fill(obj, null);
        Arrays.fill(npc, null);
        Arrays.fill(monster, null);

        // Khởi tạo lại dữ liệu cho màn 1
        aSetter.setObject();
        aSetter.setNPC();

        gameState = playState;
    }

}
