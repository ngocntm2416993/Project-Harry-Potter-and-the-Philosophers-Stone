package object;

import entity.Player;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Trapdoor extends SuperObject {
    public int targetMap;
    public int spawnTileX, spawnTileY;

    public OBJ_Trapdoor(int targetMap, int spawnTileX, int spawnTileY, int tileSize) {
        this.name       = "Cửa Sập";
        this.hintText   = "[F] Đi đến màn " + targetMap;
        this.dialogText = "Một cửa sập dẫn xuống tầng dưới...";
        this.targetMap  = targetMap;
        this.spawnTileX = spawnTileX;
        this.spawnTileY = spawnTileY;
        this.collision  = false;
        this.drawSizeW  = 3;
        this.drawSizeH  = 3;
        int inset = tileSize / 4;
        solidArea.x      = inset;
        solidArea.y      = inset;
        solidArea.width  = tileSize * 3 - inset * 2;
        solidArea.height = tileSize * 3 - inset * 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        proximityRange   = tileSize * 2;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/door/trapdoor.png"));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onInteract(GamePanel gp, Player player) {
        gp.changeMap(targetMap);
    }
}