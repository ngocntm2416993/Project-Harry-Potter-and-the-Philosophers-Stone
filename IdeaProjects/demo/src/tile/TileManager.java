package tile;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int mapTileNum [] [];
    int mapObjectNum [][];

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[30];
        mapTileNum = new int [gp.maxScreenCol][gp.maxScreenRow];
        mapObjectNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        getTileImage();
        loadMap("/maps/map4.txt");
    }

    public void getTileImage() {

        try {
            tile[0]=new Tile();
            tile[0].image= ImageIO.read(getClass().getResourceAsStream("/background/sangach3.png"));
            tile[1]=new Tile();
            tile[1].image= ImageIO.read(getClass().getResourceAsStream("/background/sangach2.png"));
            tile[2]=new Tile();
            tile[2].image= ImageIO.read(getClass().getResourceAsStream("/background/sangach.png"));
            tile[3]=new Tile();
            tile[3].image= ImageIO.read(getClass().getResourceAsStream("/background/tuongda3.png"));
            tile[4]=new Tile();
            tile[4].image= ImageIO.read(getClass().getResourceAsStream("/background/gachreuxanh1.png"));
            tile[5]=new Tile();
            tile[5].image= ImageIO.read(getClass().getResourceAsStream("/background/gachreuxanh2.png"));
            tile[6]=new Tile();
            tile[6].image= ImageIO.read(getClass().getResourceAsStream("/background/bancoden.png"));
            tile[7]=new Tile();
            tile[7].image= ImageIO.read(getClass().getResourceAsStream("/background/bancotrang.png"));
            tile[8]=new Tile();
            tile[8].image= ImageIO.read(getClass().getResourceAsStream("/door/cuabth2.png"));
            tile[9]=new Tile();
            tile[9].image= ImageIO.read(getClass().getResourceAsStream("/door/cuabth1.png"));

            // đồ deco
            tile[11]=new Tile();
            tile[11].image= ImageIO.read(getClass().getResourceAsStream("/deco/window.png"));
            tile[12]=new Tile();
            tile[12].image= ImageIO.read(getClass().getResourceAsStream("/deco/danhac.png"));
            tile[13]=new Tile();
            tile[13].image= ImageIO.read(getClass().getResourceAsStream("/deco/chest4.png"));
            tile[14]=new Tile();
            tile[14].image= ImageIO.read(getClass().getResourceAsStream("/door/congto.png"));
            tile[15]=new Tile();
            tile[15].image= ImageIO.read(getClass().getResourceAsStream("/deco/cotda2.png"));
            tile[16]=new Tile();
            tile[16].image= ImageIO.read(getClass().getResourceAsStream("/deco/ribbon.png"));
            tile[17]=new Tile();
            tile[17].image= ImageIO.read(getClass().getResourceAsStream("/deco/bookshelf.png"));
            tile[18]=new Tile();
            tile[18].image= ImageIO.read(getClass().getResourceAsStream("/deco/banthuocdoc.png"));
            tile[19]=new Tile();
            tile[19].image= ImageIO.read(getClass().getResourceAsStream("/door/cuaden.png"));
            tile[20]=new Tile();
            tile[20].image= ImageIO.read(getClass().getResourceAsStream("/door/cualuahong.png"));
            tile[21] = new Tile();
            tile[21].image= ImageIO.read(getClass().getResourceAsStream("/deco/guongthan.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // ĐỌC LAYER NỀN
            for (int row = 0; row < gp.maxScreenRow; row++) {
                String line = br.readLine();
                String numbers[] = line.split(" ");
                for (int col = 0; col < gp.maxScreenCol; col++) {
                    mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                }
            }

            // ĐỌC DÒNG NGĂN CÁCH "---"
            br.readLine();

            // ĐỌC LAYER 1: VẬT THỂ TRANG TRÍ
            for (int row = 0; row < gp.maxScreenRow; row++) {
                String line = br.readLine();
                if (line == null) break;

                String numbers[] = line.split(" ");
                for (int col = 0; col < gp.maxScreenCol; col++) {
                    mapObjectNum[col][row] = Integer.parseInt(numbers[col]);
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int tileX = 0;
        int tileY = 0;

        // VÒNG LẶP 1: VẼ LỚP NỀN (LAYER 0)
        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                int tileType = mapTileNum[col][row];
                g2.drawImage(tile[tileType].image, tileX, tileY, gp.tileSize, gp.tileSize, null);
                tileX += gp.tileSize;
            }
            tileX = 0;
            tileY += gp.tileSize;
        }

        // RESET LẠI TỌA ĐỘ ĐỂ VẼ LAYER TIẾP THEO
        tileX = 0;
        tileY = 0;

        // VÒNG LẶP 2: VẼ LỚP VẬT THỂ (LAYER 1) ĐÈ LÊN TRÊN
        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                int objectType = mapObjectNum[col][row];

                // CHỈ VẼ KHI Ô ĐÓ CÓ VẬT THỂ (Khác 0)
                if (objectType > 0) {
                    if (objectType == 15 || objectType == 16) {
                        g2.drawImage(tile[objectType].image, tileX, tileY, gp.tileSize, gp.tileSize * 2, null);
                    }else if (objectType == 11 || objectType == 12 || objectType == 14 || objectType == 17 || objectType == 20 || objectType==19 ) {
                        // Nếu là ID 11 (Vật thể kích thước 2x2 ô)
                        // Chúng ta vẽ với kích thước chiều rộng và chiều cao nhân đôi (gp.tileSize * 2)
                        g2.drawImage(tile[objectType].image, tileX, tileY, gp.tileSize * 2, gp.tileSize * 2, null);
                    }
                    else if (objectType == 18){
                        g2.drawImage(tile[objectType].image, tileX, tileY, gp.tileSize *4, gp.tileSize * 4, null);
                    }
                    else if (objectType == 21){
                        g2.drawImage(tile[objectType].image, tileX, tileY, gp.tileSize *3, gp.tileSize * 3, null);
                    }
                    else {
                        // Các vật thể kích thước 1 ô bình thường khác
                        g2.drawImage(tile[objectType].image, tileX, tileY, gp.tileSize, gp.tileSize, null);
                    }
                }
                tileX += gp.tileSize;
            }
            tileX = 0;
            tileY += gp.tileSize;
        }
    }
}
