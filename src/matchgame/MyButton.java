/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matchgame;
import javax.swing.*;

/**
 *
 * @author foxta
 */
public class MyButton extends JButton{
    
    private int value;  // keep the file name x.png (where x = 1 to 20) the cards with same bug image will have the same value
    private ImageIcon icon, dIcon;   // dIcon is Pucca image, icon is a bug image.
    
   
    
    void setIcon(){
        // show a BUG image
        this.setIcon(icon);
    }
    
    void resetIcon(){
        //***** show a PUCCA image
        /*.................*/ this.setIcon(dIcon);
    }
    
    void set(ImageIcon dIcon, ImageIcon icon,  int n){
        
        //*** initialize attributes: icon, dIcon, and value 
        
        /*...........................*/ this.dIcon = dIcon; //ans
                                        this.icon = icon;   //ans
                                        value = n;          //ans
        
        // show PUCCA image
        this.resetIcon();
    }
    
    int value(){
        // return button's value
        return value;
    }
    
}
