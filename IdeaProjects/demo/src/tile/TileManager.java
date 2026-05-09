package tile;
import main.GamePanel;
import tile.Tile;
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

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[15];
        mapTileNum = new int [gp.maxScreenCol][gp.maxScreenRow];
        getTileImage();
        loadMap("/maps/manthuocdoc.txt");
    }

    public void getTileImage() {

        try {
            tile[0]=new Tile();
            tile[0].image= ImageIO.read(getClass().getResourceAsStream("/background/bancoden.png"));
            tile[1]=new Tile();
            tile[1].image= ImageIO.read(getClass().getResourceAsStream("/background/bancotrang.png"));
            tile[2]=new Tile();
            tile[2].image= ImageIO.read(getClass().getResourceAsStream("/door/cuacungdien.png"));
            tile[3]=new Tile();
            tile[3].image= ImageIO.read(getClass().getResourceAsStream("/background/cotda.png"));
            tile[4]=new Tile();
            tile[4].image= ImageIO.read(getClass().getResourceAsStream("/background/tuongda1.png"));
            tile[5]=new Tile();
            tile[5].image= ImageIO.read(getClass().getResourceAsStream("/background/tuongda2.png"));
            tile[6]=new Tile();
            tile[6].image= ImageIO.read(getClass().getResourceAsStream("/background/tuongda3.png"));
            tile[7]=new Tile();
            tile[7].image= ImageIO.read(getClass().getResourceAsStream("/background/gachreuxanh1.png"));
            tile[8]=new Tile();
            tile[8].image= ImageIO.read(getClass().getResourceAsStream("/background/gachreuxanh2.png"));
            tile[9]=new Tile();
            tile[9].image= ImageIO.read(getClass().getResourceAsStream("/background/gachreuxanh3.png"));
            tile[10]=new Tile();
            tile[10].image= ImageIO.read(getClass().getResourceAsStream("/background/cuahamtren.png"));
            tile[11]=new Tile();
            tile[11].image= ImageIO.read(getClass().getResourceAsStream("/background/cuahamduoitrai.png"));
//            tile[12]=new Tile();
//            tile[12].image= ImageIO.read(getClass().getResourceAsStream("door/cuabth1.png"));
            tile[13]=new Tile();
            tile[13].image= ImageIO.read(getClass().getResourceAsStream("/door/cuaden.png"));
            tile[14]=new Tile();
            tile[14].image= ImageIO.read(getClass().getResourceAsStream("/door/cualuahong.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMap(String map_path) {
        try {
            InputStream is = getClass().getResourceAsStream(map_path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.maxScreenRow; row++) {

                String line = br.readLine();
                String numbers[] = line.split(" ");

                for (int col = 0; col < gp.maxScreenCol; col++) {

                    int num = Integer.parseInt(numbers[col]);
                        mapTileNum[col][row] = num;
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2) {

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gp.maxScreenCol && row < gp.maxScreenRow) {

            int tileNum = mapTileNum[col][row];
            if(tileNum == 2){
                g2.drawImage(tile[2].image, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
            }
            else if(tileNum == 3){
                g2.drawImage(tile[3].image, x, y, gp.tileSize, gp.tileSize * 2, null);
            }
            else if(tileNum == 13){
                g2.drawImage(tile[13].image, x, y, gp.tileSize *2, gp.tileSize * 2, null);
            }
            else if(tileNum == 14){
                g2.drawImage(tile[14].image, x, y, gp.tileSize *2, gp.tileSize * 2, null);
            }

            else if(tileNum == -1){
                // bỏ qua, không vẽ gì
            }
            else if(tileNum >= 0 && tileNum < tile.length){
                g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            }

            // ✅ Chỉ có 1 khối tăng col/row duy nhất
            col++;
            x += gp.tileSize;
            if(col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
//    public void draw(Graphics2D g2) {        * vẽ chay * không sử dụng matrix
//        int ts = gp.tileSize;
//        int centerCol = gp.maxScreenCol / 2;
//
//        // ===== HÀNG TRÊN (row = 0) =====
//        for (int col = 0; col < gp.maxScreenCol; col++) {
//
//            int x = col * ts;
//
//            if (col == 0 || col == gp.maxScreenCol - 1) {
//                g2.drawImage(tile[4].image, x, 0, ts, ts, null); // góc
//            }
//            else if (col == centerCol) {
//                g2.drawImage(tile[2].image, x, 0, ts, ts, null); // cửa
//            }
//            else {
//                g2.drawImage(tile[3].image, x, 0, ts, ts, null); // tường
//            }
//        }
//
//        // ===== PHẦN GIỮA (row 1 → 9) =====
//        for (int row = 1; row <= 9; row++) {
//
//            for (int col = 0; col < gp.maxScreenCol; col++) {
//
//                int x = col * ts;
//                int y = row * ts;
//
//                if (col == 0 || col == gp.maxScreenCol - 1) {
//                    // tường 2 bên
//                    g2.drawImage(tile[4].image, x, y, ts, ts, null);
//                }
//                else {
//                    // bàn cờ
//                    if ((row + col) % 2 == 0) {
//                        g2.drawImage(tile[0].image, x, y, ts, ts, null); // đen
//                    } else {
//                        g2.drawImage(tile[1].image, x, y, ts, ts, null); // trắng
//                    }
//                }
//            }
//        }
//
//        // ===== 2 HÀNG DƯỚI (row 10, 11) =====
//        for (int row = 10; row <= 11; row++) {
//
//            for (int col = 0; col < gp.maxScreenCol; col++) {
//
//                int x = col * ts;
//                int y = row * ts;
//
//                if (col == centerCol || col == centerCol - 1) {
//                    g2.drawImage(tile[7].image, x, y, ts, ts, null); // 2 ô giữa
//                }
//                else {
//                    g2.drawImage(tile[6].image, x, y, ts, ts, null);
//                }
//            }
//        }
//    }
}
