/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import javax.swing.JOptionPane;

/**
 *
 * @author Gregory Salazar
 */
public class Player extends Actor {

    private final static double LEVELING_FACTOR = Math.sqrt(MAX_XP);
    

    public Player(String name) {

        super(name, "player.png");   //set this player's name
        
    }

    public int getXpToNextLvl() {
        //returns the amount of xp that needs to be gained in order to level up
        //result will be less than or equal to zero if the xp requirement for the next level is already met
        if (level == MAX_LEVEL) {
            return 0;
        }
        int xpNeeded = (int) (Math.pow(LEVELING_FACTOR * level / (MAX_LEVEL - 1), 2)) - xp;
        return xpNeeded;
    }

    public void giveXp(int xpGain) {
        //gives the actor experience points
        xp += Math.min(xpGain, MAX_XP);
        levelCheck();
    }

    public void levelCheck() {
        //levels up the actor if xp requirements are met
        while (getXpToNextLvl() <= 0 && level < MAX_LEVEL) {
            levelUp();
        }
    }

    public void levelUp() {
        JOptionPane.showMessageDialog(Window.getFrames()[0],
                "You are now level " + (++level),
                "LEVEL UP!",
                JOptionPane.PLAIN_MESSAGE);
        System.out.println(xp);
    }
}
