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

/**
 *
 * @authors Zachary Kirchens, Gregory Salazar
 */
public class Main {
    
    public final static ResourceManager Resource = new ResourceManager();  //create the object that will retrieve and save project resources

    public static void main(String[] args) throws IOException, InterruptedException {
        
        Window w = new Window();    //create window object
        w.setVisible(true);
        BufferedImage title = Resource.loadImage("title.bmp");
        w.getGraphics().drawImage(title, 0, 0, w);  //display title image
        
        SoundSystem.playMusic("boss_music.wav"); //Source: https://www.youtube.com/watch?v=KagcLZSvLWo
        SoundSystem.playSound("sword_hit.wav"); //Source: https://www.freesound.org/people/Ceacy/sounds/2642
        
    }
}
