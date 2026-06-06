package object;

import entity.Player;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_NPC_map_3 extends SuperObject {
    public OBJ_NPC_map_3(int tileSize) {
        this.name       = "Phù Thủy";
        this.dialogs = new String[]{
                "Trước mặt nguy hiểm, sau lưng an toàn,\n" +
                "Mi sẽ tìm được, hai chai hữu ích\n",
                "Một chai uống vào, giúp mi tiến tới\n" +
                        "Một chai uống vào, mi sẽ quay lui\n",
                "Hai trong số bảy, là rượu tầm ma\n" +
                        "Trà trộn trong đó, ba chai độc dược.\n",
                "Hãy chọn một chai, uống vào giải nguy,\n" +
                        "Trừ khi mi muốn kẹt hoài ở đây.\n" +
                        "Để giúp mi chọn, có bốn gợi ý ở xung quanh đây."};
        this.hintText   = "[F] Xem hướng dẫn";
        this.collision  = true;
        this.drawSizeW  = 1;
        this.drawSizeH  = 1;
        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = tileSize;
        solidArea.height = tileSize;
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/npc/7ca040c91f883814e89a24fe4d5f8421.jpg")));
        } catch (IOException e) { e.printStackTrace(); }
    }
    @Override
    public void onInteract(GamePanel gp, Player player){
        if (dialogIndex < dialogs.length) {
            gp.ui.setDialog(name, dialogs[dialogIndex]);
            gp.gameState = gp.dialogState;
            dialogIndex++;
        } else {
            dialogIndex = 0; // Reset lại
            gp.gameState = gp.playState;
        }
    }
}
