package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D; 

public class MapTransition {
    GamePanel gp;
    private boolean transitioning = false;
    private float alpha = 0f;
    private boolean fadingOut = true;
    private int nextMapId ;
    private static final float FADE_SPEED = 0.05f;

    public MapTransition(GamePanel gp) { this.gp = gp; }

    public void startTransition(int mapId) {
        if (transitioning) return;
        this.nextMapId = mapId;
        this.transitioning = true;
        this.fadingOut = true;
        this.alpha = 0f;
    }

    public void update() {
        if (!transitioning) return;

        if (fadingOut) {
            alpha += FADE_SPEED;
            if (alpha >= 1f) {
                alpha = 1f;
                gp.currentMap = nextMapId;
                gp.tileM.loadMap("/maps/map" + nextMapId + ".txt");
                int spawnX = AssetSetter.MAP_SPAWN[nextMapId - 1][0];
                int spawnY = AssetSetter.MAP_SPAWN[nextMapId - 1][1];
                gp.player.worldX = spawnX * gp.tileSize;
                gp.player.worldY = spawnY * gp.tileSize;
                gp.player.collisionOn = false;
                gp.aSetter.setObject();
                gp.aSetter.setNPC();

                fadingOut = false;
            }
        } else {
            alpha -= FADE_SPEED;
            if (alpha <= 0f) {
                alpha = 0f;
                transitioning = false;
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (!transitioning && alpha == 0f) return;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}