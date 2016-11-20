/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Animation;

import static RPG.Main.Resource;
import java.awt.image.BufferedImage;

/**
 *
 * @author Gregory Salazar
 */
public class Sprite {

    private BufferedImage spriteSheet;
    private final int TILE_SIZE = 32;
    
    public Sprite(String fileName) {
        loadSpriteSheet(fileName);
    }

    public final void loadSpriteSheet(String fileName) {
        spriteSheet = Resource.loadImage(fileName);
    }
    
    public BufferedImage getSprite() {
        return spriteSheet;
    }

    public BufferedImage getSprite(int xGrid, int yGrid) {
        
        if (spriteSheet == null) {
            System.err.println(Sprite.class.getSimpleName() + " ERROR: No sprite sheet loaded!");
            return null;
        }
        
        return spriteSheet.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
