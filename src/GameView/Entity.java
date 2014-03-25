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
    public static final int BOMB = 555;
    
    private boolean destructable;
    private boolean isPath = false;
    private int type;
    private int PlayerPawnOn = -1;
    private ImageIcon icon;
    
    public boolean setType(int newType,ImageIcon newIcon){
    	this.type = newType;
    	this.icon = newIcon;
    	return true;
    }

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
                this.isPath = true;
            case EXIT:
                this.destructable = false;
            case BOMB:
                this.destructable = true;
        }
    }

    
    public boolean isPawnOn() {
        return PlayerPawnOn != -1;
    }

    public void setPawnOn(int playerID) {
        this.PlayerPawnOn = playerID;
    }
    
    public int getPawn(){
        return this.PlayerPawnOn;
    }

    
    public int getPlayerPawnOn() {
        return PlayerPawnOn;
    }

    
    public ImageIcon getIcon() {
        return icon;
    }

    public int getType() {
        return type;
    }

    void setEnemyPawnOn(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
