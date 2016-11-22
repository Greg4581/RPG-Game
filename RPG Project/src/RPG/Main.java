/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import Services.SoundSystem;
import Services.ResourceManager;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @authors Zachary Kirchens, Gregory Salazar
 */
public class Main {

    public final static ResourceManager Resource = new ResourceManager();  //create the object that will retrieve and save project resources

    public final static int WIDTH = 800;
    public final static int HEIGHT = 640;
    public final static int SCREEN_FPS = 60;  //frames per second as displayed to the user
    public final static int PHYSICS_TPS = 60;  //frames per second for the physics engine
    
    public final static Window MAIN_WINDOW = new Window();  //create window object
    private final static World WORLD = new World();  //create world object
    public final static Player PLAYER = new Player("Player1");  //create player object

    public static void main(String[] args) throws IOException, InterruptedException {
        
        MAIN_WINDOW.setSize(WIDTH, HEIGHT);
        MAIN_WINDOW.setVisible(true);
        BufferedImage title = Resource.loadImage("title.jpg");
        MAIN_WINDOW.getGraphics().drawImage(title, 0, 0, MAIN_WINDOW);  //display title image

        SoundSystem.initialize(new String[]{
            "techno.wav", //Source: https://www.freesound.org/people/FoolBoyMedia/sounds/242550/
            "boss_music.wav", //Source: https://www.youtube.com/watch?v=KagcLZSvLWo/
        });
        //SoundSystem.playSound("sword_hit.wav"); //Source: https://www.freesound.org/people/Ceacy/sounds/2642/
        
        PLAYER.setPosX(10);
        PLAYER.setPosY(10);
        
        int[][] tiles = new int[25][20];
        for (int[] tile : tiles) {
            for (int tileY = 0; tileY < tiles[0].length; tileY++) {
                tile[tileY] = Tile.GRASS;
            }
        }
        World.loadNewArea(new Area("Test Area", tiles, new ArrayList<>(), new String[]{}, PLAYER));
        World.initialize();
    }
}
