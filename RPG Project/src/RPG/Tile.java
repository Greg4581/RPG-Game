/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import Animation.Sprite;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gregory Salazar
 */
public abstract class Tile {

    public final static int SIZE = 32;  //size of the tiles in pixels
    
    //tiles types as integers
    public final static int VOID = 0,
            GRASS = 1,
            DIRT = 2,
            SLATE = 3,
            SAND = 4,
            WATER = 5;
    
    private final static Sprite TILES = new Sprite("tiles.png");

    public static BufferedImage getImage(int type) {
        return TILES.getSprite(type % 10, type / 10);
    }
    
    public static List<BufferedImage> getImages() {
        List<BufferedImage> images = new ArrayList<>();
        BufferedImage image = getImage(VOID);
        int index = 0;
        while (image != null) {
            index++;
            images.add(image);
            image = getImage(index);
        }
        return images;
    }
}
