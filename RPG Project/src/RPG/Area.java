/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import Services.SoundSystem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zachary Kirchens, Gregory Salazar
 */
public class Area {

    //Variables
    private final String name;
    private final int[][] tiles;    //first layer
    private final GameObject[][] entities; //second layer
    private final String[] music;
    private final Player player;

    //Constructors
    public Area(String name, int[][] tiles, List<GameObject> entities, String[] music, Player player) {
        this.name = name;
        this.tiles = tiles;
        this.entities = new GameObject[tiles.length][tiles[0].length];
        this.music = music;
        this.player = player;
        entities.stream().forEach((entity) -> {
            this.entities[(int) entity.getPosX()][(int) entity.getPosY()] = entity;
        });
    }

    public synchronized void tick() {
        //processes the whole area by moving actors where they should be in the next frame
        //and also handles events and interactions between actors and game objects
        
        //start with player
        if (player.isMoving() || player.isAnimating()) {
            if (player.isAnimating()) {
                //change offset values only
                int offsetX = player.getOffsetX();
                int offsetY = player.getOffsetY();
                double C = player.getMovementSpeed() * (double) Tile.SIZE / Main.PHYSICS_TPS;
                player.setOffsetX((int) (offsetX + player.getDirX() * C));
                player.setOffsetY((int) (offsetY + player.getDirY() * C));
                offsetX = Math.abs(player.getOffsetX());
                offsetY = Math.abs(player.getOffsetY());
                if (offsetX < C || offsetX > Tile.SIZE - C) {
                    player.setOffsetX(0);
                }
                if (offsetY < C || offsetY > Tile.SIZE - C) {
                    player.setOffsetY(0);
                }
                if (player.getOffsetX() == 0 && player.getOffsetY() == 0) {
                    player.setAnimating(false);
                }
            } else {
                int newPosX = player.getPosX() + player.getDirX();
                int newPosY = player.getPosY() + player.getDirY();
                if (getEntity(newPosX, newPosY) == null && getTile(newPosX, newPosY) != Tile.VOID) {
                    //move to new spot
                    player.setPosX(newPosX);
                    player.setPosY(newPosY);
                    player.setOffsetX(-player.getDirX() * Tile.SIZE);
                    player.setOffsetY(-player.getDirY() * Tile.SIZE);
                    player.setAnimating(true);
                } else {
                    //can't move there
                    //SoundSystem.playSound("collision.wav");    //Source: https://www.freesound.org/people/timgormly/sounds/170141/
                }
            }
        }
        player.updateAnimation();
    }

    public int getSizeX() {
        return tiles.length;
    }

    public int getSizeY() {
        return tiles[0].length;
    }

    public int getTile(int x, int y) {
        try {
            return tiles[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return 0;
    }

    public synchronized int setTile(int x, int y, int tile) {
        return tiles[x][y] = tile;
    }

    public GameObject getEntity(int x, int y) {
        try {
            return entities[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    public List<GameObject> getAllEntities() {
        //returns all entities except for the player
        List<GameObject> allEntities = new ArrayList<>();
        for (int x = 0; x < entities.length; x++) {
            for (int y = 0; y < entities[0].length; y++) {
                if (entities[x][y] != null) {
                    allEntities.add(entities[x][y]);
                }
            }
        }
        return allEntities;
    }

    public synchronized boolean setEntity(int x, int y, GameObject entity) {
        //places the entity at the specified location if it is free
        //returns true if the entity was placed, else returns false
        if (getEntity(x, y) != null) {
            return false;
        }
        entities[x][y] = entity;
        return true;
    }
}
