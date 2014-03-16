/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import GameView.MapModel;

/**
 *
 * @author abdullahadeeb
 */
public class PlayerPawn {

    public final static int RIGHT = 3412;
    public final static int FORWARD = 1234;
    public final static int BACKWARD = 4321;
    public final static int LEFT = 2143;
    int x;
    int y;
    BombsFactory bombsFactory;
    int id;
    MapModel map;

    PlayerPawn(int id, MapModel mapModel) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.bombsFactory = new BombsFactory();
        this.map = mapModel;
        this.map.setPlayerOnEntity(x, y);

    }

    public void move(int direction) {

        int newX = x;
        int newY = y;
               
        switch (direction) {
            case PlayerPawn.RIGHT:
                if (x < this.map.getWidth() - 1 && this.map.isCellPath(x + 1, y)) {
                    newX++;
                    break;
                }
                return;
            case PlayerPawn.LEFT:
                if (x > 0 && this.map.isCellPath(x - 1, y)) {
                    newX--;
                    break;
                }
                return;
            case PlayerPawn.BACKWARD:
                if (y < this.map.getHeight() - 1 && this.map.isCellPath(x, y + 1)) {
                    newY++;
                    break;
                }
                return;
            case PlayerPawn.FORWARD:
                if (y > 0 && this.map.isCellPath(x, y - 1)) {
                    newY--;
                    break;
                }
                return;
            default:
                break;

        }
        this.map.setPlayerOffEntity(x, y);
        this.map.setPlayerOnEntity(newX, newY);
        this.x = newX;
        this.y = newY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }
}
