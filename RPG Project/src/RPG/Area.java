/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import Animation.Animation;
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
        if (player.isMoving) {
            double C = player.walkSpeed * (double) Tile.SIZE / Main.PHYSICS_FPS;
            if (Math.abs(player.getOffsetX()) > 5 || Math.abs(player.getOffsetY()) > 5) {
                //change offset values only
                double offsetX = player.getOffsetX();
                double offsetY = player.getOffsetY();
                player.setOffsetX(offsetX + player.getDirX() * C);
                player.setOffsetY(offsetY + player.getDirY() * C);
                if ((int) Math.abs(player.getOffsetX()) == 0) {
                    player.setOffsetX(0);
                }
                if ((int) Math.abs(player.getOffsetY()) == 0) {
                    player.setOffsetY(0);
                }
            } else {
                int newPosX = player.getPosX() + player.getDirX();
                int newPosY = player.getPosY() + player.getDirY();
                if (getEntity(newPosX, newPosY) == null) {
                    //move to new spot
                    player.setPosX(newPosX);
                    player.setPosY(newPosY);
                    player.setOffsetX(-player.getDirX() * (Tile.SIZE - 1));
                    player.setOffsetY(-player.getDirY() * (Tile.SIZE - 1));
                } else {
                    //can't move there
                    SoundSystem.playSound("collision.wav");    //Source: https://www.freesound.org/people/timgormly/sounds/170141/
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
        return tiles[x][y];
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
