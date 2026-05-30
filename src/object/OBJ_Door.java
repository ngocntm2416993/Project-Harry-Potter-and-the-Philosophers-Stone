package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Door extends SuperObject {
    public int targetMap;
    public int spawnTileX, spawnTileY;

    public OBJ_Door(String imagePath, int targetMap, int spawnTileX, int spawnTileY, int tileSize) {
        this.name       = "Cửa";
        this.targetMap  = targetMap;
        this.spawnTileX = spawnTileX;
        this.spawnTileY = spawnTileY;
        this.hintText   = "[F] Đi đến màn " + targetMap;
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
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onInteract(main.GamePanel gp, entity.Player player) {
        gp.changeMap(targetMap);
    }
}