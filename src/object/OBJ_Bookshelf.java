package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import entity.Player;

public class OBJ_Bookshelf extends SuperObject {

    public OBJ_Bookshelf(String imagePath, String dialog, int tileSize) {
        this(imagePath, new String[]{ dialog }, tileSize);
    }

    /** Constructor mới – nhiều trang dialog */
    public OBJ_Bookshelf(String imagePath, String[] dialogs, int tileSize) {
        this.name       = "Kệ Sách";
        this.dialogs    = dialogs;
        this.hintText   = "[F] Đọc sách";
        this.collision  = true;
        this.drawSizeW  = 2;
        this.drawSizeH  = 2;
        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = tileSize * 2;
        solidArea.height = tileSize * 2;
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
        proximityRange   = tileSize * 2;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onInteract(GamePanel gp, Player player) {
        if (dialogs == null || dialogs.length == 0) {
            gp.gameState = gp.playState;
            return;
        }
        if (dialogIndex < dialogs.length) {
            gp.ui.setDialog(name, dialogs[dialogIndex]);
            gp.gameState = gp.dialogState;
            dialogIndex++;
        } else {
            dialogIndex = 0;          // reset để lần sau đọc lại từ đầu
            gp.gameState = gp.playState;
            gp.ui.setDialog("", "");
        }
    }
}