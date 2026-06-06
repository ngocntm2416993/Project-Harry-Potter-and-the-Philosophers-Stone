/*package entity;

import java.awt.List;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;

public class Mon_Boss_3 extends Entity{

    private BufferedImage[] idleFrame = new BufferedImage[7];
    private BufferedImage[] deathFrame = new BufferedImage[9];
    private int frameIndex = 0;
    private int frameCounter = 0;
    private final int FRAME_SPEED = 8; //tick mỗi frame

    public enum State{IDLE,DEATH}
    public State state = State.IDLE;
    private boolean deathDone = false; //đánh dấu animation death

    //Skill
    public List<BossSkill> skills = new ArrayList<>();
    private int shootCooldown = 0;
    private final int SHOOT_INTERVAL = 120;// bắn mỗi 2 giây
    
    private final int tileSize = 2; //Boss chiếm 2 tile

    public Mon_Boss_3(GamePanel gp, int col, int row){
        super(gp);
        this.worldX = col * gp.tileSize;
        this.worldY = row * gp.tileSize;
        this.direction = "down";
        this.speed = 0;
        
    }
}*/
