/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zachary Kirchens, Gregory Salazar
 */
public class Area {

    //Variables
    private final int[][] tiles;    //first layer
    private final List<GameObject> objects; //second layer
    private final String[] music = new String[]{};
    
    //Constructors
    public Area(String name, int sizeX, int sizeY) {
        tiles = new int[sizeX][sizeY];
        objects = new ArrayList<>();
    }
}
