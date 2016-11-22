/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import Animation.Animation;
import Animation.Sprite;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @authors Zachary Kirchens, Gregory Salazar
 */
public class Actor
        extends GameObject {

    public final static int MAX_XP = 1000000, MAX_LEVEL = 100;

    //animation images
    private static Sprite actorSprite;

    //animation states
    static Animation currentAnimation, walkLeft, walkRight, walkUp, walkDown;

    //sprite states
    final static int WALK1 = 0, STANDING = 1, WALK2 = 2;
    public final static int DOWN = 0, LEFT = 1, RIGHT = 2, UP = 3;

    //Other Variables-----------------------------------------------------------------
    private double health;
    private double maxHealth;
    private double movementSpeed;   //walk speed in tiles/sec
    String desc;
    ArrayList<GameItem> inventory;
    int xp = 0;
    int level = 1;
    private boolean isAlive, isMoving, isAnimating, isRunning;

    private int facing;
    int wantToFace;

    //Constructors--------------------------------------------------------------
    public Actor(String sName, String spriteFileName) {

        super(sName);

        maxHealth = 100;
        health = maxHealth;
        movementSpeed = 3.0;    //base movement speed
        isAlive = true;
        facing = DOWN;

        setSprite(spriteFileName);

        //load and set animation images
        BufferedImage[] walkingLeft = new BufferedImage[]{
            actorSprite.getSprite(STANDING, LEFT), actorSprite.getSprite(WALK1, LEFT), actorSprite.getSprite(STANDING, LEFT), actorSprite.getSprite(WALK2, LEFT)
        };
        BufferedImage[] walkingRight = new BufferedImage[]{
            actorSprite.getSprite(STANDING, RIGHT), actorSprite.getSprite(WALK1, RIGHT), actorSprite.getSprite(STANDING, RIGHT), actorSprite.getSprite(WALK2, RIGHT)
        };
        BufferedImage[] walkingUp = new BufferedImage[]{
            actorSprite.getSprite(STANDING, UP), actorSprite.getSprite(WALK1, UP), actorSprite.getSprite(STANDING, UP), actorSprite.getSprite(WALK2, UP)
        };
        BufferedImage[] walkingDown = new BufferedImage[]{
            actorSprite.getSprite(STANDING, DOWN), actorSprite.getSprite(WALK1, DOWN), actorSprite.getSprite(STANDING, DOWN), actorSprite.getSprite(WALK2, DOWN)
        };

        //create animation objects
        int frameDelay = (int) Math.max(1, (double) Main.PHYSICS_TPS / (this.getMovementSpeed() * 10));
        walkLeft = new Animation(walkingLeft, frameDelay);
        walkRight = new Animation(walkingRight, frameDelay);
        walkUp = new Animation(walkingUp, frameDelay);
        walkDown = new Animation(walkingDown, frameDelay);
    }

    //Mutators------------------------------------------------------------------
    public void setHealth(double iHealth) {
        health = iHealth;
        if (health <= 0) {
            this.die();
        }
    }

    public void setMaxHealth(double iMaxHealth) {
        health = iMaxHealth;
    }

    public void setDesc(String sDesc) {
        desc = sDesc;
    }

    public void setXP(int iXP) {
        xp = Math.min(iXP, MAX_XP);
    }

    public void setLevel(int iLevel) {
        level = Math.min(iLevel, MAX_LEVEL);
    }

    public void setIsAlive(boolean bIsAlive) {
        isAlive = bIsAlive;
    }

    public final void setSprite(String spriteFileName) {
        actorSprite = new Sprite(spriteFileName);
    }

    //Accessors-----------------------------------------------------------------
    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public String getDesc() {
        return desc;
    }

    public int getXP() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    //Game-Related Methods
    public void die() {
        this.setIsAlive(false);
    }

    public void getObject() {
        //for(GameObject:Area.getObjects())
    }

    public BufferedImage getCurrentSprite() {
        if (currentAnimation != null) {
            return currentAnimation.getSprite();
        } else {
            return actorSprite.getSprite(STANDING, facing);
        }
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void updateAnimation() {
        if (currentAnimation != null) {
            currentAnimation.start();
            currentAnimation.update();
        }
        if (isMoving || isAnimating) {
            switch (facing) {
                case LEFT:
                    currentAnimation = walkLeft;
                    return;
                case RIGHT:
                    currentAnimation = walkRight;
                    return;
                case UP:
                    currentAnimation = walkUp;
                    return;
                case DOWN:
                    currentAnimation = walkDown;
            }
        } else {
            currentAnimation = null;
        }
    }

    //Actor movement functions--------------------------------------------------
    public void setFacingDirection(int dir) {
        //makes the actor look in the specified direction if able to
        if (dir < 0 || dir > 3) {
            return;
        }
        wantToFace = dir;
        if (!isAnimating) {
            facing = dir;
        }
    }

    public int getFacingDirection() {
        if (!isAnimating && facing != wantToFace) {
            facing = wantToFace;
        }
        return facing;
    }

    public int getDirX() {
        if (!isAnimating && facing != wantToFace) {
            facing = wantToFace;
        }
        switch (facing) {
            case LEFT:
                return -1;
            case RIGHT:
                return 1;
        }
        return 0;
    }

    public int getDirY() {
        if (!isAnimating && facing != wantToFace) {
            facing = wantToFace;
        }
        switch (facing) {
            case UP:
                return -1;
            case DOWN:
                return 1;
        }
        return 0;
    }

    public String Affect(Effect eff, double dStrength) {
        switch (eff) {
            case HEAL: {
                if ((health + dStrength) > maxHealth) {
                    this.setHealth(maxHealth);
                    return "Health set to max!";
                } else {
                    this.setHealth(health + dStrength);
                    return "You gained " + dStrength + " health.";
                }
            }
            case HARM: {
                if (health <= dStrength) {
                    this.setHealth(0.0);
                    return "You lost all HP and DIED";
                } else {
                    this.setHealth((double) health - dStrength);
                    return "You lost " + dStrength + " health";
                }
            }
            case XP: {
                this.setXP(xp + (int) dStrength);
                return "You gained " + (int) dStrength + " experience points";
            }
            case HPMAX: {
                this.setHealth(maxHealth);
                break;
            }
            case LEVELUP: {
                this.setLevel(level + 1);
                break;
            }
            case HPMAXUP: {
                this.setMaxHealth(maxHealth + dStrength);
                break;
            }
            default: {
                System.out.println("This tried.... something. It didnt work.");
            }
        }
        return "";
    }

    public void setMoving(boolean b) {
        isMoving = b;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setRunning(boolean b) {
        isRunning = b;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setMovementSpeed(double d) {
        movementSpeed = d;
    }

    public final double getMovementSpeed() {
        if (isRunning) {
            return movementSpeed * 3;
        }
        return movementSpeed;
    }

    public void setAnimating(boolean b) {
        isAnimating = b;
    }

    public boolean isAnimating() {
        return isAnimating;
    }
}
