package tile;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Graphics2D;

import main.GamePanel;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;
    public int[][] mapObjectNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile         = new Tile[100];
        mapTileNum   = new int[gp.maxWorldRow][gp.maxWorldCol];
        mapObjectNum = new int[gp.maxWorldRow][gp.maxWorldCol];

        getTileImage();
        loadMap("/maps/map1.txt");
        fillObjectFootprint();
    }

    // ── helper: tạo tile không collision ──────────────────────────────────
    private void setup(int index, String path) {
        setup(index, path, false, null);
    }

    // ── helper: tạo tile có/không collision, solidArea tùy chọn ──────────
    private void setup(int index, String path, boolean collision, Rectangle solidArea) {
        try {
            tile[index] = new Tile();
            tile[index].image     = javax.imageio.ImageIO.read(getClass().getResourceAsStream(path));
            tile[index].collision = collision;
            tile[index].solidArea = solidArea;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── helper: solid = full NxN tile ─────────────────────────────────────
    private Rectangle full(int n) {
        int s = gp.tileSize * n;
        return new Rectangle(0, 0, s, s);
    }

    public void getTileImage() {
        int T = gp.tileSize;

        // ── NỀN ──────────────────────────────────────────────────────────
        setup(0,  "/background/san.png");
        setup(1,  "/background/san1.png");
        setup(2,  "/background/san2.png");
        setup(3,  "/background/san3.png");
        setup(43, "/background/bancoden.png");
        setup(44, "/background/bancotrang.png");

        // ── TƯỜNG LAYER 0 (full collision) ───────────────────────────────
        setup(4,  "/background/wall.png",    true, new Rectangle(0, 0, T, T));

        // ── TƯỜNG LAYER 1 (solidArea chính xác) ─────────────────────────
        setup(5,  "/background/wall_d.png",  true, new Rectangle(0, 27, T, 21));
        setup(6,  "/background/wall_u.png",  true, new Rectangle(0,  0, T, 21));
        setup(7,  "/background/wall_l.png",  true, new Rectangle(0,  0, 21, T));
        setup(8,  "/background/wall_r.png",  true, new Rectangle(27, 0, 21, T));
        setup(9,  "/background/wall_u.png",  true, new Rectangle(0,  0, T, 21));
        setup(10, "/background/wall_d.png",  true, new Rectangle(0, 27, T, 21));
        setup(11, "/background/wall_rd.png", true, new Rectangle(0,  0, T, T));
        setup(12, "/background/wall_ru.png", true, new Rectangle(0,  0, T, T));
        setup(13, "/background/wall_ld.png", true, new Rectangle(0,  0, T, T));
        setup(14, "/background/wall_lu.png", true, new Rectangle(0,  0, T, T));

        // ── CỬA / PORTAL (không collision — đi qua được) ────────────────
        setup(15, "/door/trapdoor.png",   false, full(3));
        setup(16, "/door/cua.png");
        setup(17, "/door/cuaso.png");
        setup(19, "/door/cuaden.png");
        setup(20, "/door/cualuahong.png");
        setup(39, "/door/cuaphong.png",   false, full(3));
        setup(53, "/door/luatim.png",     false, full(3));
        setup(54, "/door/luaden.png",     false, full(3));

        // ── TRANG TRÍ 1x1 ────────────────────────────────────────────────
        setup(18, "/deco/banthuocdoc.png", true,  new Rectangle(0, 8, T, 34));
        setup(21, "/deco/guongthan.png");
        setup(30, "/deco/candle.png",      true,  new Rectangle(16, 32, 16, 16));
        setup(31, "/deco/torch.png",       true,  new Rectangle(16, 32, 16, 16));
        setup(32, "/deco/stone.png",       true,  new Rectangle(4, 4, 40, 40));
        setup(40, "/deco/vase.png",        true,  new Rectangle(5, 2, 38, 43));
        setup(41, "/deco/vase2.png",       true,  new Rectangle(6, 0, 37, 42));
        setup(49, "/deco/web.png");
        setup(61, "/deco/pp1.png");
        setup(62, "/deco/pp2.png");
        setup(63, "/deco/scroll.png");

        // ── TRANG TRÍ 1x3 (cột đứng) ─────────────────────────────────────
        setup(37, "/deco/cotduoc.png", true, new Rectangle(8, 0, 32, T * 3));
        setup(51, "/deco/cot.png",     true, new Rectangle(8, 0, 32, T * 3));
        setup(52, "/deco/ribbon.png",  true, new Rectangle(8, 0, 32, T * 3));

        // ── TRANG TRÍ 2x2 ────────────────────────────────────────────────
        setup(22, "/deco/bookshelf.png",  true, full(2));
        setup(23, "/deco/bookshelf2.png", true, full(2));
        setup(24, "/deco/bookshelf3.png", true, full(2));
        setup(26, "/deco/barrow.png",     true, new Rectangle(24, 36, 48, 60));
        setup(27, "/deco/barrow2.png",    true, new Rectangle(24, 31, 48, 65));
        setup(28, "/deco/becua.png",      true, full(2));
        setup(33, "/deco/thunggo1.png",   true, new Rectangle(7,  27, 84, 69));
        setup(34, "/deco/thunggo2.png",   true, new Rectangle(4,  27, 84, 69));
        setup(35, "/deco/thunggo3.png",   true, new Rectangle(7,  33, 84, 63));
        setup(36, "/deco/thunggo4.png",   true, new Rectangle(4,  33, 84, 63));
        setup(38, "/deco/kiem.png",       true, full(2));
        setup(42, "/deco/table.png",      true, full(2));
        setup(58, "/deco/bookpile.png",   true, new Rectangle(18, 2, 54, 80));
        setup(59, "/deco/bookpile2.png",  true, new Rectangle(13,0,54,80));
        setup(60, "/deco/giasach.png",    true, full(2));

        // ── TRANG TRÍ 3x3 ────────────────────────────────────────────────
        setup(25, "/deco/bookshelf4.png", true, full(3));
        setup(29, "/deco/becua2.png",     true, full(3));
        setup(45, "/deco/buc1.png",       true, full(3));
        setup(46, "/deco/buc2.png",       true, full(3));
        setup(47, "/deco/buc3.png",       true, full(3));
        setup(48, "/deco/buc4.png",       true, full(3));
        setup(50, "/deco/fallenchess.png",true, new Rectangle(0, 58, 144, 81));

        // ── TRANG TRÍ 4x4 ────────────────────────────────────────────────
        setup(56, "/deco/banchinh.png",   true, new Rectangle(4, 60, T*4-8, T*4-60));
        setup(64, "/deco/guongthan.png",  true, full(4));

        // ── TRANG TRÍ 6x6 ────────────────────────────────────────────────
        setup(55, "/deco/bansach.png",    true, new Rectangle(54, 63, 190, 141));
        setup(57, "/deco/carpet.png",     false, full(6));
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                if (line == null) break;
                String[] nums = line.split(" ");
                for (int col = 0; col < gp.maxWorldCol; col++)
                    mapTileNum[row][col] = Integer.parseInt(nums[col]);
            }

            br.readLine(); // dòng "---"

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                if (line == null) break;
                String[] nums = line.split(" ");
                for (int col = 0; col < gp.maxWorldCol; col++)
                    mapObjectNum[row][col] = Integer.parseInt(nums[col]);
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Lỗi nạp map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Đánh dấu tất cả ô phụ mà object lớn chiếm bằng mã âm encode vị trí ô neo.
     * CollisionChecker dùng để tìm về ô neo khi check collision.
     */
    private void fillObjectFootprint() {
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                int objType = mapObjectNum[row][col];
                if (objType <= 0) continue;

                Tile t = tile[objType];
                if (t == null || t.solidArea == null) continue;

                int startCol = t.solidArea.x / gp.tileSize;
                int endCol   = (int) Math.ceil((double)(t.solidArea.x + t.solidArea.width)  / gp.tileSize);
                int startRow = t.solidArea.y / gp.tileSize;
                int endRow   = (int) Math.ceil((double)(t.solidArea.y + t.solidArea.height) / gp.tileSize);

                int anchorCode = -(row * gp.maxWorldCol + col + 1);

                for (int r = row + startRow; r < Math.min(row + endRow, gp.maxWorldRow); r++) {
                    for (int c = col + startCol; c < Math.min(col + endCol, gp.maxWorldCol); c++) {
                        if (r == row && c == col) continue;
                        if (mapObjectNum[r][c] == 0 || mapObjectNum[r][c] < 0)
                            mapObjectNum[r][c] = anchorCode;
                    }
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        int T = gp.tileSize;

        // LAYER 0: NỀN
        for (int worldRow = 0; worldRow < gp.maxWorldRow; worldRow++) {
            for (int worldCol = 0; worldCol < gp.maxWorldCol; worldCol++) {
                int wx = worldCol * T;
                int wy = worldRow * T;
                int sx = wx - gp.player.worldX + gp.player.screenX;
                int sy = wy - gp.player.worldY + gp.player.screenY;

                if (wx > gp.player.worldX - gp.player.screenX - T &&
                    wx < gp.player.worldX + gp.player.screenX + T &&
                    wy > gp.player.worldY - gp.player.screenY - T &&
                    wy < gp.player.worldY + gp.player.screenY + T) {
                    int t = mapTileNum[worldRow][worldCol];
                    if (tile[t] != null)
                        g2.drawImage(tile[t].image, sx, sy, T, T, null);
                }
            }
        }

        // LAYER 1: VẬT THỂ (chỉ vẽ ô neo > 0)
        for (int worldRow = 0; worldRow < gp.maxWorldRow; worldRow++) {
            for (int worldCol = 0; worldCol < gp.maxWorldCol; worldCol++) {
                int ot = mapObjectNum[worldRow][worldCol];
                if (ot <= 0) continue;

                int wx = worldCol * T;
                int wy = worldRow * T;
                int sx = wx - gp.player.worldX + gp.player.screenX;
                int sy = wy - gp.player.worldY + gp.player.screenY;

                if (wx > gp.player.worldX - gp.player.screenX - T * 4 &&
                    wx < gp.player.worldX + gp.player.screenX + T * 4 &&
                    wy > gp.player.worldY - gp.player.screenY - T * 4 &&
                    wy < gp.player.worldY + gp.player.screenY + T * 4) {

                    if (tile[ot] == null) continue;
                    int drawW, drawH;

                    // 1x3
                    if (ot == 37 || ot == 51 || ot == 52) {
                        drawW = T; drawH = T * 3;
                    }
                    // 2x2
                    else if ((ot >= 22 && ot <= 24) || ot == 26 || ot == 27 || ot == 28
                            || (ot >= 33 && ot <= 36) || ot == 38 || ot == 42
                            || (ot >= 58 && ot <= 60)) {
                        drawW = T * 2; drawH = T * 2;
                    }
                    // 3x3
                    else if (ot == 25 || ot == 29 || (ot >= 15 && ot <= 17)
                            || ot == 39 || (ot >= 45 && ot <= 48)
                            || ot == 50 || ot == 53 || ot == 54) {
                        drawW = T * 3; drawH = T * 3;
                    }
                    // 4x4
                    else if (ot == 56 || ot == 64) {
                        drawW = T * 4; drawH = T * 4;
                    }
                    // 6x6
                    else if (ot == 55 || ot == 57) {
                        drawW = T * 6; drawH = T * 6;
                    }
                    // 1x1
                    else {
                        drawW = T; drawH = T;
                    }

                    g2.drawImage(tile[ot].image, sx, sy, drawW, drawH, null);
                }
            }
        }
    }
}
