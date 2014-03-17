/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

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

    Entity(int type) {
        this.type = type;
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

    public int getType() {
        return type;
    }
}
