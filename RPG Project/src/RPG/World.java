/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import Animation.Animation;
import static RPG.Main.HEIGHT;
import static RPG.Main.PHYSICS_TPS;
import static RPG.Main.PLAYER;
import static RPG.Main.WIDTH;
import Services.SoundSystem;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zachary Kirchens, Gregory Salazar
 */
public final class World implements Serializable, Runnable {

    public final static BufferedImage WORLD_IMAGE = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    public final static List<BufferedImage> TILE_IMAGES = Tile.getImages(); //load tile images
    public static boolean paused;

    private static Thread worldThread;
    private static Area currentArea;

    //Variables-----------------------------------------------------------------
    private final String name;
    private final Area[][] areas;

    //Constructors--------------------------------------------------------------
    public World() {
        name = "Default World";
        areas = new Area[10][10];
        worldThread = new Thread(this, "World");
        worldThread.setPriority(9);
    }

    public String getName() {
        return name;
    }

    public static synchronized void initialize() {
        if (worldThread.isAlive()) {
            return;
        }
        worldThread.start();
    }

    public static synchronized void pause() {
        //pause the game
        paused = true;
        SoundSystem.pauseSounds();
        SoundSystem.pauseMusic();
    }

    public static synchronized void unPause() {
        //unpauses the game
        paused = false;
        SoundSystem.unPauseMusic();
        SoundSystem.unPauseSounds();
    }

    private static synchronized void updateWorldImage() {

        Graphics g = WORLD_IMAGE.getGraphics();
        g.fillRect(0, 0, WIDTH, WIDTH); //clear previous world image

        //render background tiles
        for (int tileX = 0; tileX < currentArea.getSizeX(); tileX++) {
            for (int tileY = 0; tileY < currentArea.getSizeY(); tileY++) {
                g.drawImage(TILE_IMAGES.get(currentArea.getTile(tileX, tileY)), tileX * 32, tileY * 32, null);
            }
        }

        //render game objects
        List<GameObject> entities = currentArea.getAllEntities();
        entities.add(PLAYER);
        for (Iterator it = entities.iterator(); it.hasNext();) {
            GameObject obj = (GameObject) it.next();
            if (obj == null) {
                break;
            }
            if (obj instanceof Actor) {
                Actor actor = (Actor) obj;
                Animation ani = actor.getCurrentAnimation();
                BufferedImage sprite;
                if (ani != null) {
                    sprite = ani.getSprite();
                } else {
                    sprite = actor.getCurrentSprite();
                }
                //get position in pixels
                int posX = (int) (actor.getPosX() * Tile.SIZE + actor.getOffsetX());
                int posY = (int) (actor.getPosY() * Tile.SIZE + actor.getOffsetY());
                g.drawImage(sprite, posX, posY, null);
            }
        }
    }

    @Override
    public void run() {
        WORLD_IMAGE.createGraphics().setColor(Color.BLACK); //set default color to black
        while (true) {
            if (!paused && currentArea != null) {
                currentArea.tick();
                updateWorldImage();
            }
            try {
                Thread.sleep(1000 / PHYSICS_TPS);
            } catch (InterruptedException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static synchronized void loadNewArea(Area newArea) {
        currentArea = newArea;
    }
}
