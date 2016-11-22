/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zachary Kirchens, Gregory Salazar
 */
public abstract class GameObject
        implements Serializable {

    //Variables-----------------------------------------------------------------
    private final static List<GameObject> instances = new ArrayList<>();    //should keep track of all created game objects until removal or death
    boolean collidable, stationary;
    private int posX;
    private int posY;
    
    //precise position offset values that the world renderer considers when rending the object
    private int offsetX;
    private int offsetY;
    
    private String name;

    //Constructors--------------------------------------------------------------
    public GameObject(String sName) {
        this.setName(sName);
        this.setPosX(0);
        this.setPosY(0);
        instances.add(this);
    }

    public GameObject(String sName, int x, int y) {
        this.setName(sName);
        this.setPosX(x);
        this.setPosY(y);
        instances.add(this);
    }

    //Mutators------------------------------------------------------------------
    public static void removeInstance(GameObject obj) {
        //finds the specified object in the instances list and removes it if found
        instances.remove(obj);
    }

    public final void setName(String sName) {
        name = sName;
    }

    public final void setPosX(int x) {
        posX = x;
    }

    public final void setPosY(int y) {
        posY = y;
    }

    public void setOffsetX(int offset) {
        offsetX = (int) (Math.signum(offset) * Math.min(Math.abs(offset), Tile.SIZE));
    }

    public void setOffsetY(int offset) {
        offsetY = (int) (Math.signum(offset) * Math.min(Math.abs(offset), Tile.SIZE));
    }

    //Accessors-----------------------------------------------------------------
    public static List getInstances() {
        //returns a list of all the instances
        return instances;
    }

    public String getName() {
        return name;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
