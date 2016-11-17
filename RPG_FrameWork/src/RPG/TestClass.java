/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Zachary Kirchens
 */
public class TestClass {
    
    final static ResourceLoader RL = new ResourceLoader();  //create the factory object that will retrieve project resources

    public static void main(String[] args) throws IOException {
        Window w = new Window();    //create window object
        BufferedImage title = RL.loadImage("title.bmp");
        w.add(new JLabel(new ImageIcon(title)));
        w.pack();
        w.setVisible(true);
        Area a = new Area();
    }
}
