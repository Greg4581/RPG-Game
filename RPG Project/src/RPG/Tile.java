/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import Animation.Sprite;
import java.awt.image.BufferedImage;

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
    
    private final static Sprite tiles = new Sprite("tiles.png");

    public static BufferedImage getImage(int type) {
        return tiles.getSprite(type % 10, type / 10);
    }
}
