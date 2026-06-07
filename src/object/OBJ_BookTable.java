package object;

import entity.Player;
import main.GamePanel;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_BookTable extends SuperObject {
    public boolean isPuzzleTable;
    public boolean isUsed = false;
    public String[] choices = {"White", "Green", "Yellow", "Blue", "Red", "Black", "Purple"};
    public OBJ_BookTable(String imagePath,boolean isPuzzleTable, int drawW, int drawH, String[] dialogs, int tileSize) {
        this.isPuzzleTable = isPuzzleTable;
        this.name       = "Bàn Sách";
        this.dialogs = (dialogs != null && dialogs.length > 0) ? dialogs : new String[]{""};
        this.hintText   = "[F] Xem bàn";
        this.collision  = true;
        this.drawSizeW  = drawW;
        this.drawSizeH  = drawH;

        // Offset y để player đi sát vào mặt bàn được
        int offsetY = tileSize / 2; // 24px — bỏ phần trống phía trên
        solidArea.x      = 4;
        solidArea.y      = offsetY;
        solidArea.width  = tileSize * drawW - 8;
        solidArea.height = tileSize * drawH - offsetY;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        proximityRange = tileSize * 2;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    @Override
    public void onInteract(GamePanel gp, Player player){
        if (isPuzzleTable){
            // Nếu còn dialog chưa đọc hết thì hiện dialog trước
            if (dialogs != null && dialogs.length > 0
                    && !(dialogs.length == 1 && dialogs[0].isEmpty())
                    && dialogIndex < dialogs.length) {
                gp.ui.setDialog(name, dialogs[dialogIndex]);
                gp.gameState = gp.dialogState;
                dialogIndex++;
            } else {
                // Đọc xong dialog (hoặc không có) thì mở puzzle
                dialogIndex = 0;
                gp.currentObject = this;
                gp.gameState = gp.puzzleState;
            }
        } else {
            if (dialogIndex < dialogs.length) {
                gp.ui.setDialog(name, dialogs[dialogIndex]);
                gp.gameState = gp.dialogState;
                dialogIndex++;
            } else {
                dialogIndex = 0;
                gp.gameState = gp.playState;
            }
        }
    }

    public void applyEffect(int index, GamePanel gp, entity.Player player) {
        switch(index) {
            case 0: case 1: case 2:
                gp.isDoorUnlocked = true;
                player.HP = 1;
                gp.ui.showMessage("Trúng kịch độc! HP còn 1.");
                break;
            case 3:
                gp.isDoorUnlocked = true;
                gp.ui.showMessage("An toàn. Cửa đã được mở");
                break;
            case 4: case 5:
                gp.isDoorUnlocked = true;
                player.HP = Math.max(1, player.HP - 50);
                gp.ui.showMessage("Chai rượu, vấp cỏ -50HP.");
                break;
            case 6:
                gp.resetGame();
                gp.ui.showMessage("Trò chơi thất bại!");
                break;
        }

        if (player.HP <= 0) gp.gameState = gp.gameOverState;
    }
}
