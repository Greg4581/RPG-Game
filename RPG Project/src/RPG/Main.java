/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import Animation.Animation;
import Services.SoundSystem;
import Services.ResourceManager;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Zachary Kirchens, Gregory Salazar
 */
public class Main {

    public final static ResourceManager Resource = new ResourceManager();  //create the object that will retrieve and save project resources

    final static int WIDTH = 800;
    final static int HEIGHT = 640;
    public final static int SCREEN_FPS = 30;  //frames per second as displayed to the user
    public final static int PHYSICS_FPS = 30;  //frames per second for the physics engine

    public static boolean paused;

    private static Window window;
    private static Area world;
    static BufferedImage worldImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    static Player player;

    public static void main(String[] args) throws IOException, InterruptedException {

        window = new Window();    //create window object
        window.setSize(WIDTH, HEIGHT);
        window.setVisible(true);
        BufferedImage title = Resource.loadImage("title.bmp");
        window.getGraphics().drawImage(title, 0, 0, window);  //display title image

        SoundSystem.initialize(new String[]{
            "techno.wav", //Source: https://www.freesound.org/people/FoolBoyMedia/sounds/242550/
            "boss_music.wav", //Source: https://www.youtube.com/watch?v=KagcLZSvLWo/
        });
        //SoundSystem.playSound("sword_hit.wav"); //Source: https://www.freesound.org/people/Ceacy/sounds/2642/
        SoundSystem.printPlaylist();

        player = new Player("Player1");
        player.setLocationX(10);
        player.setLocationY(10);
        /*while (p.getLevel() < Player.MAX_LEVEL) {
         p.giveXp(p.getXpToNextLvl());
         }*/
        worldImage.createGraphics();
        while (true) {
            if (!paused) {
                synchronized (worldImage) {
                    processWorld();
                }
            }
            Thread.sleep(1000 / PHYSICS_FPS);
        }
    }

    public static void pause() {
        //pause the game
        paused = true;
        SoundSystem.pauseSounds();
        SoundSystem.pauseMusic();
    }

    public static void unPause() {
        //unpauses the game
        synchronized (worldImage) {
            paused = false;
            SoundSystem.unPauseMusic();
            SoundSystem.unPauseSounds();
        }
    }

    public static void processWorld() {

        Graphics g = worldImage.getGraphics();
        g.fillRect(0, 0, WIDTH, WIDTH); //clear previous world image

        //render background tiles
        int numTilesX = WIDTH / 32;
        int numTilesY = HEIGHT / 32;
        for (int tileX = 0; tileX < numTilesX; tileX++) {
            for (int tileY = 0; tileY < numTilesY; tileY++) {
                g.drawImage(Tile.getImage(Tile.GRASS), tileX * 32, tileY * 32, null);
            }
        }

        //render game objects
        List objects = GameObject.getInstances();
        for (Iterator it = objects.iterator(); it.hasNext();) {
            GameObject obj = (GameObject) it.next();
            if (obj == null) {
                break;
            }
            if (obj instanceof Actor) {
                Actor actor = (Actor) obj;
                Animation ani = actor.getCurrentAnimation();
                BufferedImage sprite;
                if (ani != null) {
                    ani.update();
                    sprite = ani.getSprite();
                } else {
                    sprite = actor.getCurrentSprite();
                }
                //get position in pixels
                int posX = (int) actor.getLocX() * Tile.SIZE + actor.getOffsetX();
                int posY = (int) actor.getLocY() * Tile.SIZE + actor.getOffsetY();
                g.drawImage(sprite, posX, posY, null);
            }
        }
    }
}
