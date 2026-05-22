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

        tile = new Tile[30];
        // mapTileNum = new int [gp.maxScreenCol][gp.maxScreenRow];
        // mapObjectNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];
        mapObjectNum = new int[gp.maxWorldRow][gp.maxWorldCol];

        getTileImage();
        loadMap("/maps/man1.txt");
    }

    public void getTileImage ()  {

        try {
            tile[0]=new Tile();
            tile[0].image= ImageIO.read(getClass().getResourceAsStream("/tile/gach3.png"));
            tile[1]=new Tile();
            tile[1].image= ImageIO.read(getClass().getResourceAsStream("/tile/gach2.png"));
            tile[2]=new Tile();
            tile[2].image= ImageIO.read(getClass().getResourceAsStream("/tile/gach1.png"));
            tile[3]=new Tile();
            tile[3].image= ImageIO.read(getClass().getResourceAsStream("/tile/tuong32.png"));
            tile[4]=new Tile();
            tile[4].image= ImageIO.read(getClass().getResourceAsStream("/tile/reu1.png"));
            tile[5]=new Tile();
            tile[5].image= ImageIO.read(getClass().getResourceAsStream("/tile/reu2.png"));
            tile[6]=new Tile();
            tile[6].image= ImageIO.read(getClass().getResourceAsStream("/tile/den.png"));
            tile[7]=new Tile();
            tile[7].image= ImageIO.read(getClass().getResourceAsStream("/tile/trang.png"));
            tile[8]=new Tile();
            tile[8].image= ImageIO.read(getClass().getResourceAsStream("/tile/trapdoor2.png"));
            tile[9]=new Tile();
            tile[9].image= ImageIO.read(getClass().getResourceAsStream("/tile/trapdoor.png"));
            // đồ deco
            tile[11]=new Tile();
            tile[11].image= ImageIO.read(getClass().getResourceAsStream("/tile/window.png"));
            tile[12]=new Tile();
            tile[12].image= ImageIO.read(getClass().getResourceAsStream("/tile/dan.png"));
            tile[13]=new Tile();
            tile[13].image= ImageIO.read(getClass().getResourceAsStream("/tile/chest4.png"));
            tile[14]=new Tile();
            tile[14].image= ImageIO.read(getClass().getResourceAsStream("/tile/cong1.png"));
            tile[15]=new Tile();
            tile[15].image= ImageIO.read(getClass().getResourceAsStream("/tile/cot.png"));
            tile[16]=new Tile();
            tile[16].image= ImageIO.read(getClass().getResourceAsStream("/tile/ribbon.png"));
            tile[17]=new Tile();
            tile[17].image= ImageIO.read(getClass().getResourceAsStream("/tile/bookshelf.png"));
            tile[18]=new Tile();
            tile[18].image= ImageIO.read(getClass().getResourceAsStream("/tile/ban.png"));
            tile[19]=new Tile();
            tile[19].image= ImageIO.read(getClass().getResourceAsStream("/tile/den1.png"));
            tile[20]=new Tile();
            tile[20].image= ImageIO.read(getClass().getResourceAsStream("/tile/hong1.png"));
            tile[21] = new Tile();
            tile[21].image= ImageIO.read(getClass().getResourceAsStream("/tile/guong.png"));

            tile[22] = new Tile();
            tile[22].image= ImageIO.read(getClass().getResourceAsStream("/tile/tuong1.png"));
            tile[23] = new Tile();
            tile[23].image= ImageIO.read(getClass().getResourceAsStream("/tile/tuong2.png"));
            tile[24] = new Tile();
            tile[24].image= ImageIO.read(getClass().getResourceAsStream("/tile/tuong3.png"));
            tile[25] = new Tile();
            tile[25].image= ImageIO.read(getClass().getResourceAsStream("/tile/water.png"));

        }catch (IOException e) {
            e.printStackTrace();
        }
}


    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;

            while (row < gp.maxWorldRow) {
                String line = br.readLine();

                if (line == null) break; // tránh lỗi

                String[] numbers = line.split(" ");

                for (int col = 0; col < gp.maxWorldCol; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[row][col] = num;
                }

                row++;
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw (Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldRow][worldCol];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if (worldX > gp.player.worldX - gp.player.screenX - gp.tileSize &&
                worldX < gp.player.worldX + gp.player.screenX + gp.tileSize &&
                worldY > gp.player.worldY - gp.player.screenY  - gp.tileSize &&
                worldY < gp.player.worldY + gp.player.screenY + gp.tileSize) {
                    g2.drawImage (tile[tileNum].image, screenX, screenY,gp.tileSize, gp.tileSize, null);
                }
            worldCol ++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow ++;
            }
        }
    }
}

