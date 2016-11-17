/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import java.io.Serializable;

/**
 *
 * @author Zachary Kirchens, Gregory Salazar
 */
public class World implements Serializable {

    //Variables-----------------------------------------------------------------
    private final String name;
    private final Area[][] world;

    //Constructors--------------------------------------------------------------
    public World() {
        name = "Default World";
        world = new Area[10][10];
    }

    public World(String name, int iSizeX, int iSizeY) {
        this.name = name;
        world = new Area[iSizeX][iSizeY];
    }

    public String getName() {
        return name;
    }
}
