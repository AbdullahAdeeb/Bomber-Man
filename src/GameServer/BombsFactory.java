/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author abdullahadeeb
 */
class BombsFactory {

    private int bombsDeployed;
    private int bombsAllowance;
    private Timer detonationTimer;
    private int range;
    private long detonationDelay = 2000;

    public BombsFactory() {
        bombsDeployed = 0;
        bombsAllowance = 1;//maximum number of bombsAllowed, works like a semaphore
        range = 1; //only the cells next to the bomb will be exploded



    }

    public synchronized void dropBomb(TimerTask dt) {
        System.out.println("bomb dropped");
        synchronized (this) {
            if (bombsAllowance == 0) {
                return;
            }
            bombsDeployed++;
            bombsAllowance--;
            new Timer().schedule(dt, this.detonationDelay);

        }

    }

    public synchronized int detonateBomb() {
        synchronized (this) {
            System.out.println("BOMB detonted");
            bombsDeployed--;
            bombsAllowance++;
            return range;
        }

    }

    public synchronized void increaseRange() {
        if (range < 5) {
            range++;
        }
    }

    public long getDetonationDelay() {
        return detonationDelay;
    }

    public void setDetonationDelay(long detonationDelay) {
        this.detonationDelay = detonationDelay;
    }

    public synchronized void increaseAllowance() {
        bombsAllowance++;
    }
}
