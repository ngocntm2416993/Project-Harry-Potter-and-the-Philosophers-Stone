package main;

import java.awt.Rectangle;

import entity.Entity;
import object.SuperObject;
import tile.Tile;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // =====================================================================
    // CHECK TILE LAYER 0 (nền)
    // =====================================================================
    public void checkTile(Entity entity) {
        int leftX   = entity.worldX + entity.solidAreaDefaultX;
        int rightX  = entity.worldX + entity.solidAreaDefaultX + entity.solidArea.width;
        int topY    = entity.worldY + entity.solidAreaDefaultY;
        int bottomY = entity.worldY + entity.solidAreaDefaultY + entity.solidArea.height;

        int leftCol   = leftX   / gp.tileSize;
        int rightCol  = rightX  / gp.tileSize;
        int topRow    = topY    / gp.tileSize;
        int bottomRow = bottomY / gp.tileSize;

        switch (entity.direction) {
            case "up": {
                topRow = (topY - entity.speed) / gp.tileSize;
                if (topRow < 0) { entity.collisionOn = true; break; }
                if (isTileCollision(topRow, leftCol) || isTileCollision(topRow, rightCol))
                    entity.collisionOn = true;
                break;
            }
            case "down": {
                bottomRow = (bottomY + entity.speed) / gp.tileSize;
                if (bottomRow >= gp.maxWorldRow) { entity.collisionOn = true; break; }
                if (isTileCollision(bottomRow, leftCol) || isTileCollision(bottomRow, rightCol))
                    entity.collisionOn = true;
                break;
            }
            case "left": {
                leftCol = (leftX - entity.speed) / gp.tileSize;
                if (leftCol < 0) { entity.collisionOn = true; break; }
                if (isTileCollision(topRow, leftCol) || isTileCollision(bottomRow, leftCol))
                    entity.collisionOn = true;
                break;
            }
            case "right": {
                rightCol = (rightX + entity.speed) / gp.tileSize;
                if (rightCol >= gp.maxWorldCol) { entity.collisionOn = true; break; }
                if (isTileCollision(topRow, rightCol) || isTileCollision(bottomRow, rightCol))
                    entity.collisionOn = true;
                break;
            }
        }

        // Check layer object với solidArea chính xác
        checkObjectLayer(entity);
    }

    private boolean isTileCollision(int row, int col) {
        if (row < 0 || row >= gp.maxWorldRow || col < 0 || col >= gp.maxWorldCol) return true;
        int tileNum = gp.tileM.mapTileNum[row][col];
        return gp.tileM.tile[tileNum] != null && gp.tileM.tile[tileNum].collision;
    }

    // =====================================================================
    // CHECK LAYER 1 (object) — dùng solidArea chính xác
    // =====================================================================
    private void checkObjectLayer(Entity entity) {
        int nextX = entity.worldX + entity.solidAreaDefaultX;
        int nextY = entity.worldY + entity.solidAreaDefaultY;
        int w = entity.solidArea.width;
        int h = entity.solidArea.height;

        switch (entity.direction) {
            case "up":    nextY -= entity.speed; break;
            case "down":  nextY += entity.speed; break;
            case "left":  nextX -= entity.speed; break;
            case "right": nextX += entity.speed; break;
        }

        Rectangle entityRect = new Rectangle(nextX, nextY, w, h);

        int margin = 7;
        int leftCol   = Math.max(0, nextX / gp.tileSize - margin);
        int rightCol  = Math.min(gp.maxWorldCol - 1, (nextX + w) / gp.tileSize + margin);
        int topRow    = Math.max(0, nextY / gp.tileSize - margin);
        int bottomRow = Math.min(gp.maxWorldRow - 1, (nextY + h) / gp.tileSize + margin);

        for (int row = topRow; row <= bottomRow; row++) {
            for (int col = leftCol; col <= rightCol; col++) {
                int objType = gp.tileM.mapObjectNum[row][col];
                if (objType == 0) continue;
                int anchorRow = row, anchorCol = col;

                if (objType < 0) {
                    int anchorIndex = (-objType) - 1;
                    anchorRow = anchorIndex / gp.maxWorldCol;
                    anchorCol = anchorIndex % gp.maxWorldCol;
                    objType = gp.tileM.mapObjectNum[anchorRow][anchorCol];
                    if (objType <= 0) continue;
                }

                Tile t = gp.tileM.tile[objType];
                if (t == null || !t.collision) continue;

                Rectangle objSolid;
                if (t.solidArea != null) {
                    objSolid = new Rectangle(
                        anchorCol * gp.tileSize + t.solidArea.x,
                        anchorRow * gp.tileSize + t.solidArea.y,
                        t.solidArea.width,
                        t.solidArea.height
                    );
                } else {
                    objSolid = new Rectangle(
                        anchorCol * gp.tileSize,
                        anchorRow * gp.tileSize,
                        gp.tileSize,
                        gp.tileSize
                    );
                }

                if (entityRect.intersects(objSolid)) {
                    entity.collisionOn = true;
                    return;
                }
            }
        }
    }

    // =====================================================================
    // CHECK OBJECT (SuperObject array)
    // =====================================================================
    public int checkObject(Entity entity, boolean player) {
        Rectangle entityRect = getEntityRect(entity);
        Rectangle futureRect = new Rectangle(entityRect);
        switch (entity.direction) {
            case "up":    futureRect.y -= entity.speed; break;
            case "down":  futureRect.y += entity.speed; break;
            case "left":  futureRect.x -= entity.speed; break;
            case "right": futureRect.x += entity.speed; break;
        }

        int index = -1;
        for (int i = 0; i < gp.obj.length; i++) {
            SuperObject obj = gp.obj[i];
            if (obj == null) continue;
            Rectangle objRect = getObjRect(obj);
            if (futureRect.intersects(objRect)) {
                if (obj.collision) entity.collisionOn = true;
                if (player) index = i;
            }
        }
        return index;
    }

    public int checkObjectInteraction(Entity entity) {
        int reach = gp.tileSize + 8;
        Rectangle checkRect = new Rectangle(getEntityRect(entity));
        switch (entity.direction) {
            case "up":    checkRect.y -= reach; break;
            case "down":  checkRect.y += reach; break;
            case "left":  checkRect.x -= reach; break;
            case "right": checkRect.x += reach; break;
        }
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) continue;
            if (checkRect.intersects(getObjRect(gp.obj[i]))) return i;
        }
        return -1;
    }

    public int checkDoorTile(Entity entity) {
        int cx = entity.worldX + entity.solidAreaDefaultX + entity.solidArea.width  / 2;
        int cy = entity.worldY + entity.solidAreaDefaultY + entity.solidArea.height / 2;
        int reach = gp.tileSize + 16;
        switch (entity.direction) {
            case "up":    cy -= reach; break;
            case "down":  cy += reach; break;
            case "left":  cx -= reach; break;
            case "right": cx += reach; break;
        }
        int col = cx / gp.tileSize;
        int row = cy / gp.tileSize;
        if (row < 0 || row >= gp.maxWorldRow || col < 0 || col >= gp.maxWorldCol) return 0;
        int objTile = gp.tileM.mapObjectNum[row][col];
        if (objTile > 0) return objTile;

        int maxSpread = 6;
        for (int dr = -maxSpread; dr <= 0; dr++) {
            for (int dc = -maxSpread; dc <= 0; dc++) {
                int nr = row + dr, nc = col + dc;
                if (nr < 0 || nc < 0 || nr >= gp.maxWorldRow || nc >= gp.maxWorldCol) continue;
                int nearObj = gp.tileM.mapObjectNum[nr][nc];
                if (nearObj <= 0) continue;
                int sz = getObjectTileSize(nearObj);
                if (row >= nr && row < nr + sz && col >= nc && col < nc + sz) return nearObj;
            }
        }
        return 0;
    }

    // =====================================================================
    // CHECK Entity
    // =====================================================================

    public int checkEntity(Entity entity, Entity[] target) {
        int index = -1;
        for (int i = 0; i < target.length; i++) {
            if (target[i] != null && target[i] != entity) {
                Rectangle entityRect = getEntityRect(entity);
                Rectangle targetRect = getEntityRect(target[i]);

                switch (entity.direction) {
                    case "up":    entityRect.y -= entity.speed; break;
                    case "down":  entityRect.y += entity.speed; break;
                    case "left":  entityRect.x -= entity.speed; break;
                    case "right": entityRect.x += entity.speed; break;
                }

                if (entityRect.intersects(targetRect)) {
                    entity.collisionOn = true;
                    index = i;
                }
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;
        Rectangle entityRect = getEntityRect(entity);
        Rectangle playerRect = getEntityRect(gp.player);

        switch (entity.direction) {
            case "up":    entityRect.y -= entity.speed; break;
            case "down":  entityRect.y += entity.speed; break;
            case "left":  entityRect.x -= entity.speed; break;
            case "right": entityRect.x += entity.speed; break;
        }

        if (entityRect.intersects(playerRect)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        return contactPlayer;
    }
    // =====================================================================
    // HELPERS
    // =====================================================================
    private Rectangle getEntityRect(Entity entity) {
        return new Rectangle(
            entity.worldX + entity.solidAreaDefaultX,
            entity.worldY + entity.solidAreaDefaultY,
            entity.solidArea.width,
            entity.solidArea.height
        );
    }

    private Rectangle getObjRect(SuperObject obj) {
        return new Rectangle(
            obj.worldX + obj.solidAreaDefaultX,
            obj.worldY + obj.solidAreaDefaultY,
            obj.solidArea.width,
            obj.solidArea.height
        );
    }

    public static int getObjectTileSize(int objectType) {
        if ((objectType >= 26 && objectType <= 36 && objectType != 32 && objectType != 29)
                || objectType == 38 || objectType == 42
                || (objectType >= 22 && objectType <= 24)
                || (objectType >= 58 && objectType <= 62)) return 2;
        if ((objectType >= 15 && objectType <= 17) || objectType == 39
                || (objectType >= 45 && objectType <= 48)
                || objectType == 50 || objectType == 53 || objectType == 54
                || objectType == 25 || objectType == 29) return 3;
        if (objectType == 57 || objectType == 55) return 6;
        if (objectType == 56 || objectType == 64) return 4;
        return 1;
    }
}
