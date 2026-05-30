package main;

import object.*;
import java.util.Arrays;

import entity.Mon_Boss_1;
import entity.Mon_Chess;

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

    private SuperObject createObject(int tileId, int worldX, int worldY,
                                     int nextMap, int spawnX, int spawnY) {
        final int ts = gp.tileSize;
        SuperObject obj;

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
            case 70:
                obj = new OBJ_NPC_map_1(ts);
                break;
            case 71:
                obj = new OBJ_NPC_map_2(ts);
                break;
            case 72:
                obj = new OBJ_NPC_map_3(ts);
                break;
            case 73:
                obj = new OBJ_NPC_map_4(ts);
                break;
            default:
                return null;
        }

        obj.worldX = worldX;
        obj.worldY = worldY;
        return obj;
    }

    private String getDoorFile(int tileId) {
        return switch (tileId) {
            case 19 -> "cuaden.png";
            case 20 -> "cualuahong.png";
            default -> "cua.png";
        };
    }

    public void setNPC() {
    Arrays.fill(gp.npc, null); // xóa NPC cũ
    if (gp.currentMap == 1) {
        // Boss đứng trước trapdoor tại (28,27) → đặt ở (27, 24) để đứng phía trên
        gp.npc[0] = new Mon_Boss_1(gp, 12, 14);
    }

    if (gp.currentMap == 2) {
        int L = 3, R = 26;
        int leftX  = L * gp.tileSize;       // spawn bên trái
        int rightX = (R - 2) * gp.tileSize; // spawn bên phải, lùi 2 tile cho chắc

        gp.npc[1]  = new Mon_Chess(gp, L, 5,  L, R, "/chess/codenthap");
        gp.npc[1].worldX = leftX;
        gp.npc[11] = new Mon_Chess(gp, L, 5,  L, R, "/chess/cotrangcao");
        gp.npc[11].worldX = rightX;

        gp.npc[2]  = new Mon_Chess(gp, L, 7,  L, R, "/chess/conguaden");
        gp.npc[2].worldX = leftX;
        gp.npc[12] = new Mon_Chess(gp, L, 7,  L, R, "/chess/codencao");
        gp.npc[12].worldX = rightX;

        gp.npc[3]  = new Mon_Chess(gp, L, 9,  L, R, "/chess/cotrangcao");
        gp.npc[3].worldX = leftX;
        gp.npc[13] = new Mon_Chess(gp, L, 9,  L, R, "/chess/codenthap");
        gp.npc[13].worldX = rightX;

        gp.npc[4]  = new Mon_Chess(gp, L, 13, L, R, "/chess/cotrangthap");
        gp.npc[4].worldX = leftX;
        gp.npc[14] = new Mon_Chess(gp, L, 13, L, R, "/chess/cotrangcao");
        gp.npc[14].worldX = rightX;

        gp.npc[9]  = new Mon_Chess(gp, L, 11, L, R, "/chess/codencao");
        gp.npc[9].worldX = leftX;
        gp.npc[17] = new Mon_Chess(gp, L, 11, L, R, "/chess/cotrangcao");
        gp.npc[17].worldX = rightX;

        gp.npc[16] = new Mon_Chess(gp, L, 19, L, R, "/chess/cotrangcao");
        gp.npc[16].worldX = leftX;
        gp.npc[7]  = new Mon_Chess(gp, L, 19, L, R, "/chess/conguaden");
        gp.npc[7].worldX = rightX;

        gp.npc[15] = new Mon_Chess(gp, L, 17, L, R, "/chess/codenthap");
        gp.npc[15].worldX = leftX;
        gp.npc[6]  = new Mon_Chess(gp, L, 17, L, R, "/chess/codenthap");
        gp.npc[6].worldX = rightX;

        // hàng 15 chỉ có 1 con
        gp.npc[5]  = new Mon_Chess(gp, L, 15, L, R, "/chess/codencao");
        gp.npc[8]  = new Mon_Chess(gp, L, 15, L, R, "/chess/cotrangcao");
        gp.npc[5].worldX = leftX;
        gp.npc[8].worldX = rightX;
    }
    // map khác: để trống hoặc thêm NPC khác sau
}
}
