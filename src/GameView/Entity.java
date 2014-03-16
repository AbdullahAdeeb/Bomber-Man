/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import javax.swing.ImageIcon;

/**
 *
 * @author abdullahadeeb
 */
public class Entity {

    public static final int WALL = 111;
    public static final int BOX = 222;
    public static final int PATH = 333;
    public static final int EXIT = 444;
    private boolean destructable;
    private boolean path = false;
    private int type;
    private boolean isPlayerPawnOn = false;
    private ImageIcon icon;

    Entity(int type,ImageIcon icon) {
        this.type = type;
        this.icon = icon;
        switch (type) {
            case WALL:
                this.destructable = false;
            case BOX:
                this.destructable = true;
            case PATH:
                this.destructable = false;
                this.path = true;
            case EXIT:
                this.destructable = false;
        }
    }

    
    public boolean isIsPlayerPawnOn() {
        return isPlayerPawnOn;
    }

    public void setIsPlayerPawnOn(boolean isPlayerPawnOn) {
        this.isPlayerPawnOn = isPlayerPawnOn;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public int getType() {
        return type;
    }
}
