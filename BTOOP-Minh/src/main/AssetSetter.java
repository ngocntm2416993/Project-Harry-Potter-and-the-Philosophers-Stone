package main;

import entity.NPC_OldMan;
import object.OBJ_Door;
import object.OBJ_HP;
import object.OBJ_Speed;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        gp.obj[0] = new OBJ_Speed();
        gp.obj[0].worldX = 7 * gp.tileSize;
        gp.obj[0].worldY = 23 * gp.tileSize;

        gp.obj[1] = new OBJ_Door();
        gp.obj[1].worldX = 26 * gp.tileSize;
        gp.obj[1].worldY = 21 * gp.tileSize;

        gp.obj[2] = new OBJ_HP();
        gp.obj[2].worldX = 24 * gp.tileSize;
        gp.obj[2].worldY = 18 * gp.tileSize;
    }

    public void setNPC() {
        gp.npc[0] = new NPC_OldMan(gp, 5,  5);
        gp.npc[1] = new NPC_OldMan(gp, 10, 5);
        gp.npc[2] = new NPC_OldMan(gp, 15, 5);
        gp.npc[3] = new NPC_OldMan(gp, 20, 10);
        gp.npc[4] = new NPC_OldMan(gp, 8,  20);
        gp.npc[5] = new NPC_OldMan(gp, 18, 22);
        gp.npc[6] = new NPC_OldMan(gp, 25, 15);
    }
}
