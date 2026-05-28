package main;

import entity.NPC_OldMan;
import object.*;
import java.util.Arrays;

public class AssetSetter {

    GamePanel gp;

    // Vị trí spawn khi chuyển map (tileX, tileY)
    static final int[][] MAP_SPAWN = {
        {28, 27},
        {15, 29},
        {14, 29},
        {15, 29},
    };

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        Arrays.fill(gp.obj, null);
        int currentMapIdx = gp.currentMap - 1;
        int nextMapIdx    = (currentMapIdx >= 3) ? 0 : currentMapIdx + 1;
        int spawnX = MAP_SPAWN[nextMapIdx][0];
        int spawnY = MAP_SPAWN[nextMapIdx][1];

        int idx = 0;
        for (int row = 0; row < gp.maxWorldRow && idx < gp.obj.length; row++) {
            for (int col = 0; col < gp.maxWorldCol && idx < gp.obj.length; col++) {
                int tileId = gp.tileM.mapObjectNum[row][col];
                SuperObject obj = createObject(tileId, col * gp.tileSize, row * gp.tileSize,
                        nextMapIdx + 1, spawnX, spawnY);
                if (obj != null) gp.obj[idx++] = obj;
            }
        }
    }

    
    public void setNPC() {
        // Làm sạch danh sách NPC cũ trước khi nạp vị trí mới cho map
        Arrays.fill(gp.npc, null);

        gp.npc[0] = new NPC_OldMan(gp);

        // Kiểm tra dựa theo Map hiện tại
        switch (gp.currentMap) {
            case 1:
                gp.npc[0].worldX = gp.tileSize * 15;
                gp.npc[0].worldY = gp.tileSize * 26;
                break;
            case 2:
                gp.npc[0].worldX = gp.tileSize * 8;
                gp.npc[0].worldY = gp.tileSize * 14;
                break;
            case 4:
                gp.npc[0].worldX = gp.tileSize * 20;
                gp.npc[0].worldY = gp.tileSize * 15;
                break;
        }  
    }

    private SuperObject createObject(int tileId, int worldX, int worldY,
                                     int nextMap, int spawnX, int spawnY) {
        final int ts = gp.tileSize;
        SuperObject obj = null;

        switch (tileId) {
            case 15:
                obj = new OBJ_Trapdoor(nextMap, spawnX, spawnY, ts);
                break;
            case 16: case 19: case 20:
                obj = new OBJ_Door("/door/" + getDoorFile(tileId), nextMap, spawnX, spawnY, ts);
                break;
            case 39:
                obj = new OBJ_Door("/door/cuaphong.png", nextMap, spawnX, spawnY, ts);
                break;
            case 53:
                obj = new OBJ_Door("/door/luatim.png", nextMap, spawnX, spawnY, ts);
                break;
            case 54:
                obj = new OBJ_Door("/door/luaden.png", nextMap, spawnX, spawnY, ts);
                break;
            case 21:
                obj = new OBJ_Mirror(ts);
                obj.collision = false;
                break;
            case 64:
                obj = new OBJ_Mirror(ts);
                obj.collision = true;
                break;
            case 22:
                obj = new OBJ_Bookshelf("/deco/bookshelf.png",
                        "Kệ sách chứa những cuốn sách cũ kỹ...", ts);
                break;
            case 23:
                obj = new OBJ_Bookshelf("/deco/bookshelf2.png",
                        "Một quyển sách mở ra: 'Chương I: Bí mật của căn hầm...'", ts);
                break;
            case 24:
                obj = new OBJ_Bookshelf("/deco/bookshelf3.png",
                        "Sách bụi phủ dày, tựa như đã lâu không ai chạm vào.", ts);
                break;
            case 25: {
                OBJ_Bookshelf bs = new OBJ_Bookshelf("/deco/bookshelf4.png",
                        "Một cuốn nhật ký bị xé rách... chỉ còn vài trang.", ts);
                bs.drawSizeW        = 3;
                bs.drawSizeH        = 3;
                bs.solidArea.width  = ts * 3;
                bs.solidArea.height = ts * 3;
                obj = bs;
                break;
            }
            case 50:
                obj = new OBJ_FallenChess(ts);
                break;
            case 55:
                obj = new OBJ_BookTable("/deco/bansach.png", 6, 6,
                        "Một chiếc bàn phủ đầy sách và giấy tờ.", ts);
                break;
            case 56:
                obj = new OBJ_BookTable("/deco/banchinh.png", 4, 4,
                        "Chiếc bàn chính — trên đó có một tấm bản đồ bí ẩn.", ts);
                break;
            default:
                return null;
        }

        if (obj != null) {
            obj.worldX = worldX;
            obj.worldY = worldY;
        }
        return obj;
    }

    private String getDoorFile(int tileId) {
        switch (tileId) {
            case 16: return "cua.png";
            case 19: return "cuaden.png";
            case 20: return "cualuahong.png";
            default: return "cua.png";
        }
    }
}
