/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import RPG.World;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Gregory Salazar
 */
public final class ResourceManager {

    final static String MAIN_RESOURCE_DIR = "Resources";    //the name of the main resources directory

    public BufferedImage loadImage(String imageName) {
        //finds and returns an image of the specified image name in the resources directory
        //if the image could not be found, returns null
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource(MAIN_RESOURCE_DIR + "/Images/" + imageName));
        } catch (IOException ex) {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    public URL loadSound(String soundName) {
        URL url = getClass().getClassLoader().getResource(MAIN_RESOURCE_DIR + "/Sounds/" + soundName);
        if (url == null) {
            System.err.println("Sound file '" + soundName + "' does not exist.");
        }
        return url;
    }
    
    public URL loadMusic(String musicName) {
        URL url = getClass().getClassLoader().getResource(MAIN_RESOURCE_DIR + "/Music/" + musicName);
        if (url == null) {
            System.err.println("Music file '" + musicName + "' does not exist.");
        }
        return url;
    }

    public World loadWorld(String worldName) {
        World world = null;
        try {
            URL url = getClass().getClassLoader().getResource(MAIN_RESOURCE_DIR + "/Worlds/" + worldName);
            try (ObjectInputStream in = new ObjectInputStream(url.openStream())) {
                world = (World) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ERROR: The world '" + worldName + "' does not exist.");
        }
        return world;
    }
}
