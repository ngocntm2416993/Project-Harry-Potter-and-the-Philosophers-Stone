package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

import java.awt.Graphics2D;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    public int mapObjectNum [][];

    public TileManager (GamePanel gp) {
        this.gp = gp;

        tile = new Tile[100];
        // mapTileNum = new int [gp.maxScreenCol][gp.maxScreenRow];
        // mapObjectNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];
        mapObjectNum = new int[gp.maxWorldRow][gp.maxWorldCol];

        getTileImage();
        loadMap("/maps/map1.txt");
    }

     public void getTileImage() {

        try {
            tile[0]=new Tile();
            tile[0].image= ImageIO.read(getClass().getResourceAsStream("/background/san.png"));
            tile[1]=new Tile();
            tile[1].image= ImageIO.read(getClass().getResourceAsStream("/background/san1.png"));
            tile[2]=new Tile();
            tile[2].image= ImageIO.read(getClass().getResourceAsStream("/background/san2.png"));
            tile[3]=new Tile();
            tile[3].image= ImageIO.read(getClass().getResourceAsStream("/background/san3.png"));
            tile[4]=new Tile();
            tile[4].image= ImageIO.read(getClass().getResourceAsStream("/background/wall.png"));
            tile[5]=new Tile();
            tile[5].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_d.png"));
            tile[6]=new Tile();
            tile[6].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_u.png"));
            tile[7]=new Tile();
            tile[7].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_l.png"));
            tile[8]=new Tile();
            tile[8].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_r.png"));
            tile[9]=new Tile();
            tile[9].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_u.png"));
            tile[10]=new Tile();
            tile[10].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_d.png"));
            tile[11]=new Tile();
            tile[11].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_rd.png"));
            tile[12]=new Tile();
            tile[12].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_ru.png"));
            tile[13]=new Tile();
            tile[13].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_ld.png"));
            tile[14]=new Tile();
            tile[14].image= ImageIO.read(getClass().getResourceAsStream("/background/wall_lu.png"));
            tile[15]=new Tile();
            tile[15].image= ImageIO.read(getClass().getResourceAsStream("/door/trapdoor.png"));
            tile[16]=new Tile();
            tile[16].image= ImageIO.read(getClass().getResourceAsStream("/door/cua.png"));
            tile[17]=new Tile();
            tile[17].image= ImageIO.read(getClass().getResourceAsStream("/door/cuaso.png"));
            tile[18]=new Tile();
            tile[18].image= ImageIO.read(getClass().getResourceAsStream("/deco/banthuocdoc.png"));
            tile[19]=new Tile();
            tile[19].image= ImageIO.read(getClass().getResourceAsStream("/door/cuaden.png"));
            tile[20]=new Tile();
            tile[20].image= ImageIO.read(getClass().getResourceAsStream("/door/cualuahong.png"));

            tile[22] = new Tile();
            tile[22].image= ImageIO.read(getClass().getResourceAsStream("/deco/bookshelf.png"));
            tile[23] = new Tile();
            tile[23].image= ImageIO.read(getClass().getResourceAsStream("/deco/bookshelf2.png"));
            tile[24] = new Tile();
            tile[24].image= ImageIO.read(getClass().getResourceAsStream("/deco/bookshelf3.png"));
            tile[25] = new Tile();
            tile[25].image= ImageIO.read(getClass().getResourceAsStream("/deco/bookshelf4.png"));
            tile[26] = new Tile();
            tile[26].image= ImageIO.read(getClass().getResourceAsStream("/deco/barrow.png"));
            tile[27] = new Tile();
            tile[27].image= ImageIO.read(getClass().getResourceAsStream("/deco/barrow2.png"));
            tile[28] = new Tile();
            tile[28].image= ImageIO.read(getClass().getResourceAsStream("/deco/becua.png"));
            tile[29] = new Tile();
            tile[29].image= ImageIO.read(getClass().getResourceAsStream("/deco/becua2.png"));
            tile[30] = new Tile();
            tile[30].image= ImageIO.read(getClass().getResourceAsStream("/deco/candle.png"));
            tile[31] = new Tile();
            tile[31].image= ImageIO.read(getClass().getResourceAsStream("/deco/torch.png"));
            tile[32] = new Tile();
            tile[32].image= ImageIO.read(getClass().getResourceAsStream("/deco/stone.png"));
            tile[33] = new Tile();
            tile[33].image= ImageIO.read(getClass().getResourceAsStream("/deco/thunggo1.png"));
            tile[34] = new Tile();
            tile[34].image= ImageIO.read(getClass().getResourceAsStream("/deco/thunggo2.png"));
            tile[35] = new Tile();
            tile[35].image= ImageIO.read(getClass().getResourceAsStream("/deco/thunggo3.png"));
            tile[36] = new Tile();
            tile[36].image= ImageIO.read(getClass().getResourceAsStream("/deco/thunggo4.png"));
            tile[37] = new Tile();
            tile[37].image= ImageIO.read(getClass().getResourceAsStream("/deco/cotduoc.png"));
            tile[38] = new Tile();
            tile[38].image= ImageIO.read(getClass().getResourceAsStream("/deco/kiem.png"));
            tile[39] = new Tile();
            tile[39].image= ImageIO.read(getClass().getResourceAsStream("/door/cuaphong.png"));
            tile[40] = new Tile();
            tile[40].image= ImageIO.read(getClass().getResourceAsStream("/deco/vase.png"));
            tile[41] = new Tile();
            tile[41].image= ImageIO.read(getClass().getResourceAsStream("/deco/vase2.png"));
            tile[42] = new Tile();
            tile[42].image= ImageIO.read(getClass().getResourceAsStream("/deco/table.png"));
            tile[43] = new Tile();
            tile[43].image= ImageIO.read(getClass().getResourceAsStream("/background/bancoden.png"));
            tile[44] = new Tile();
            tile[44].image= ImageIO.read(getClass().getResourceAsStream("/background/bancotrang.png"));
            tile[45] = new Tile();
            tile[45].image= ImageIO.read(getClass().getResourceAsStream("/deco/buc1.png"));
            tile[46] = new Tile();
            tile[46].image= ImageIO.read(getClass().getResourceAsStream("/deco/buc2.png"));
            tile[47] = new Tile();
            tile[47].image= ImageIO.read(getClass().getResourceAsStream("/deco/buc3.png"));
            tile[48] = new Tile();
            tile[48].image= ImageIO.read(getClass().getResourceAsStream("/deco/buc4.png"));
            tile[49] = new Tile();
            tile[49].image= ImageIO.read(getClass().getResourceAsStream("/deco/web.png"));
            tile[50] = new Tile();
            tile[50].image= ImageIO.read(getClass().getResourceAsStream("/deco/fallenchess.png"));
            tile[51] = new Tile();
            tile[51].image= ImageIO.read(getClass().getResourceAsStream("/deco/cot.png"));
            tile[52] = new Tile();
            tile[52].image= ImageIO.read(getClass().getResourceAsStream("/deco/ribbon.png"));
            tile[53] = new Tile();
            tile[53].image= ImageIO.read(getClass().getResourceAsStream("/door/luatim.png"));
            tile[54] = new Tile();
            tile[54].image= ImageIO.read(getClass().getResourceAsStream("/door/luaden.png"));
            tile[55] = new Tile();
            tile[55].image= ImageIO.read(getClass().getResourceAsStream("/deco/bansach.png"));
            tile[56] = new Tile();
            tile[56].image= ImageIO.read(getClass().getResourceAsStream("/deco/banchinh.png"));
            tile[57] = new Tile();
            tile[57].image= ImageIO.read(getClass().getResourceAsStream("/deco/carpet.png"));
            tile[58] = new Tile();
            tile[58].image= ImageIO.read(getClass().getResourceAsStream("/deco/bookpile.png"));
            tile[59] = new Tile();
            tile[59].image= ImageIO.read(getClass().getResourceAsStream("/deco/bookpile2.png"));
            tile[60] = new Tile();
            tile[60].image= ImageIO.read(getClass().getResourceAsStream("/deco/giasach.png"));
            tile[61] = new Tile();
            tile[61].image= ImageIO.read(getClass().getResourceAsStream("/deco/pp1.png"));
            tile[62] = new Tile();
            tile[62].image= ImageIO.read(getClass().getResourceAsStream("/deco/pp2.png"));
            tile[63] = new Tile();
            tile[63].image= ImageIO.read(getClass().getResourceAsStream("/deco/scroll.png"));
            tile[64] = new Tile();
            tile[64].image= ImageIO.read(getClass().getResourceAsStream("/deco/guongthan.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


     public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // ĐỌC LAYER NỀN (Duyệt theo kích thước WORLD)
            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                if (line == null) break;
                String numbers[] = line.split(" ");
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    mapTileNum[row][col] = Integer.parseInt(numbers[col]);
                }
            }

            // ĐỌC DÒNG NGĂN CÁCH "---"
            br.readLine();

            // ĐỌC LAYER 1: VẬT THỂ TRANG TRÍ (Duyệt theo kích thước WORLD)
            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                if (line == null) break;

                String numbers[] = line.split(" ");
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    mapObjectNum[row][col] = Integer.parseInt(numbers[col]);
                }
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Lỗi nạp map camera di chuyển: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
    
    // --- VÒNG LẶP 1: VẼ TOÀN BỘ LỚP NỀN (LAYER 0) ---
    for (int worldRow = 0; worldRow < gp.maxWorldRow; worldRow++) {
        for (int worldCol = 0; worldCol < gp.maxWorldCol; worldCol++) {
            
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Kiểm tra camera cơ bản cho ô nền (1x1)
            if (worldX > gp.player.worldX - gp.player.screenX - gp.tileSize &&
                worldX < gp.player.worldX + gp.player.screenX + gp.tileSize &&
                worldY > gp.player.worldY - gp.player.screenY - gp.tileSize &&
                worldY < gp.player.worldY + gp.player.screenY + gp.tileSize) {
                
                int tileType = mapTileNum[worldRow][worldCol];
                g2.drawImage(tile[tileType].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }

    // --- VÒNG LẶP 2: VẼ LỚP VẬT THỂ (LAYER 1) ĐÈ LÊN TRÊN ---
    for (int worldRow = 0; worldRow < gp.maxWorldRow; worldRow++) {
        for (int worldCol = 0; worldCol < gp.maxWorldCol; worldCol++) {
            
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            int objectType = mapObjectNum[worldRow][worldCol];

            if (objectType > 0) {
                // NỚI RỘNG BIÊN CAMERA: Cho phép vẽ sớm hơn và ẩn muộn hơn để vật thể to (3x3) không bị cắt góc
                if (worldX > gp.player.worldX - gp.player.screenX - gp.tileSize * 4 &&
                    worldX < gp.player.worldX + gp.player.screenX + gp.tileSize * 4 &&
                    worldY > gp.player.worldY - gp.player.screenY - gp.tileSize * 4 &&
                    worldY < gp.player.worldY + gp.player.screenY + gp.tileSize * 4) {

                    if ((objectType >= 26 && objectType<=36 && objectType != 32 && objectType != 29) || objectType ==38 || objectType == 42 || (objectType >=22 && objectType <=24) || (objectType >=58 && objectType <=62)) {
                        g2.drawImage(tile[objectType].image, screenX, screenY, gp.tileSize * 2, gp.tileSize * 2, null);
                    } else if ((objectType >= 15 && objectType <= 17) || objectType == 39 || (objectType >=45 && objectType <=48) || objectType ==50 || objectType ==53 || objectType ==54 || objectType ==25 || objectType ==29)
                    {
                        // Vẽ ô số 15 to rộng 3x3 ô gạch hoàn chỉnh
                        g2.drawImage(tile[objectType].image, screenX, screenY, gp.tileSize * 3, gp.tileSize * 3, null);
                    } else if (objectType == 37 || objectType ==51 || objectType ==52) {
                        g2.drawImage(tile[objectType].image, screenX, screenY, gp.tileSize, gp.tileSize * 3, null);
                    }else if (objectType == 57 || objectType ==55) {
                        g2.drawImage(tile[objectType].image, screenX, screenY, gp.tileSize*6, gp.tileSize *6, null);
                    }else if (objectType == 56 || objectType ==64) {
                        g2.drawImage(tile[objectType].image, screenX, screenY, gp.tileSize*4, gp.tileSize *4, null);
                    }else {
                        g2.drawImage(tile[objectType].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                }
            }
        }
    }
}


}

