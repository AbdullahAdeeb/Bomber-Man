/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import GameView.MapModel;
import java.util.TimerTask;

/**
 *
 * @author abdullahadeeb
 */
public class PlayerPawn {

    public final static int RIGHT = 1;
    public final static int FORWARD = 2;
    public final static int BACKWARD = 3;
    public final static int LEFT = 4;
    public final static int BOMB = 5;
    private int lifes = 1;
    private int x;
    private int y;
    private BombsFactory bombsFactory;
    private int id;
    private MapModel map;

    public PlayerPawn(int id, MapModel mapModel) {
        this.id = id;


        this.bombsFactory = new BombsFactory();

        this.map = mapModel;
        int[] pos = this.map.findPosForNewPlayer();
        this.x = pos[0];
        this.y = pos[1];
        this.map.setPlayerOnEntity(id, x, y);

    }

    public void action(int direction) {

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
            case PlayerPawn.BOMB:
                System.out.println("BOMB RECEVED");
                this.bombsFactory.dropBomb(new DetonationTask(this.x, this.y));
                this.map.putBombOn(x, y);
                return;
            default:
                System.out.println("NOT IMPELMENTED COMMAND RECIEVED");
                break;

        }

        this.map.setPlayerOffEntity(x, y);
        if (this.map.isCellHavePawnOn(newX, newY)) {
            this.lifes--; //player dies if collides with an enemy
            newX = 0;
            newY = 0;
        } else {
            this.map.setPlayerOnEntity(id, newX, newY);
        }
        this.x = newX;
        this.y = newY;
    }

    public boolean isDead() {
        return this.lifes == 0;
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

    class DetonationTask extends TimerTask {

        int x;
        int y;

        public DetonationTask(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
            // this is the timer that will run when the bomb explodes
            int range = bombsFactory.detonateBomb();
            map.setBombOff(this.x, this.y, range);
            System.out.println("bomb EXploded");
        }
    }

}
