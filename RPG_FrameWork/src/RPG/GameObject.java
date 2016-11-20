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
    boolean collidable;
    private double LocationX;
    private double LocationY;
    //position offset values that the world renderer considers when rending the object
    private int offsetX;
    private int offsetY;
    private String name;

    //Constructors--------------------------------------------------------------
    public GameObject(String sName) {
        this.setName(sName);
        this.setLocationX(0.0);
        this.setLocationY(0.0);
        instances.add(this);
    }

    public GameObject(String sName, double dLocX, double sLocY) {
        this.setName(sName);
        this.setLocationX(dLocX);
        this.setLocationY(sLocY);
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

    public final void setLocationX(double dLocX) {
        LocationX = dLocX;
    }

    public final void setLocationY(double dLocY) {
        LocationY = dLocY;
    }

    public void setOffsetX(int offset) {
        offsetX = offset;
    }

    public void setOffsetY(int offset) {
        offsetY = offset;
    }

    //Accessors-----------------------------------------------------------------
    public static List getInstances() {
        //returns a list of all the instances
        return instances;
    }

    public String getName() {
        return name;
    }

    public double getLocX() {
        return LocationX;
    }

    public double getLocY() {
        return LocationY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
