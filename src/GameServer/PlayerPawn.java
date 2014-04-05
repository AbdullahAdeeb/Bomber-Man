/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import GameView.MapModel;
import java.net.InetAddress;
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
    private boolean isDead;
    private int bombAllowance;
    private int currentBombs;
    private InetAddress ip;
    boolean checking = false;
    boolean thePower = false;
    private int powerupRange;

    public PlayerPawn(int id, InetAddress ip, MapModel mapModel) {
        this.id = id;
        this.ip = ip;
        bombAllowance = 1;
        currentBombs = 0;
        powerupRange = 1;

        this.bombsFactory = new BombsFactory();

        this.map = mapModel;
        int[] pos = this.map.findPosForNewPlayer();
        this.x = pos[0];
        this.y = pos[1];
        this.map.setPlayerOnEntity(id, x, y);
        isDead = false;

    }
    
    synchronized public void incRange(){
    	powerupRange++;
    }
    
    synchronized public void incBombs(){
    	bombAllowance++;
    }
    
    synchronized public int getRange(){
    	return powerupRange;
    }

    public void action(int action) {

        int newX = x;
        int newY = y;

        switch (action) {
            case PlayerPawn.RIGHT:
                if (x < this.map.getWidth() - 1 && this.map.isCellPath(x + 1, y)) {
                    newX++;
                    break;
                }
               
                if (x < this.map.getWidth() - 1 && this.map.isCellExit(x + 1, y) && (!this.map.isExitHidden())) {
                    newX++;
                    checking = true;
                    break;
                }
                if (x < this.map.getWidth() - 1 && this.map.isPowerUp(x + 1, y)) {
                    thePower = true;
                    newX++;
                    map.putPathIn(newX, newY);
                    break;
                }
                return;
            case PlayerPawn.LEFT:
                if (x > 0 && this.map.isCellPath(x - 1, y)) {
                    newX--;
                    break;
                }
                if (x > 0 - 1 && this.map.isCellExit(x - 1, y) && (!this.map.isExitHidden())) {
                    newX--;
                    checking = true;
                    break;
                }
                if (x > 0 - 1 && this.map.isPowerUp(x - 1, y)) {
                    thePower = true;
                    newX--;
                    map.putPathIn(newX, newY);
                    break;
                }
                return;
            case PlayerPawn.BACKWARD:
                if (y < this.map.getHeight() - 1 && this.map.isCellPath(x, y + 1)) {
                    newY++;
                    break;
                }
                if (y < this.map.getHeight() - 1 && this.map.isCellExit(x , y+1) && (!this.map.isExitHidden())) {
                    newY++;
                    checking = true;
                    break;
                }
                if (y < this.map.getHeight() - 1 && this.map.isPowerUp(x , y +1)) {
                    thePower = true;
                    newY++;
                    map.putPathIn(newX, newY);
                    break;
                }
                return;
            case PlayerPawn.FORWARD:
                if (y > 0 && this.map.isCellPath(x, y - 1)) {
                    newY--;
                    break;
                }
                if (y > 0 - 1 && this.map.isCellExit(x , y-1) && (!this.map.isExitHidden())) {
                    newY--;
                    checking = true;
                    break;
                }
                if (y > 0 - 1 && this.map.isPowerUp(x , y -1)) {
                    thePower = true;
                    newY--;
                    map.putPathIn(newX, newY);
                    break;
                }
                return;
            case PlayerPawn.BOMB:
                System.out.println("BOMB RECEVED");

                //Only allow bombs to be dropped if you haven't reached the limit
                if (!(currentBombs >= bombAllowance)) {
                    currentBombs++;
                    this.bombsFactory.dropBomb(new DetonationTask(this.x, this.y, this));
                    this.map.putBombOn(x, y);
                }

                return;
            default:
                System.out.println("NOT IMPELMENTED COMMAND RECIEVED");
                break;

        }

        this.map.setPlayerOffEntity(x, y);
        /**
         * if (this.map.isCellHavePawnOn(newX, newY)) { this.setDeath();
         * this.lifes--; //player dies if collides with an enemy //newX = 0;
         * //newY = 0; } else { //Only sets them there if the player is not dead
         * if (!this.isDead){ this.map.setPlayerOnEntity(id, newX, newY); }
         *
         * }
         *
         */
        if (!this.isDead) {
            this.map.setPlayerOnEntity(id, newX, newY);
        }
        this.x = newX;
        this.y = newY;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDeath() {
        this.map.setPlayerOffEntity(x, y);
        isDead = true;

        //This is done so they don't remain on the board after death
        //x = -1;
        //y = -1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public int getId() {
        return id;
    }

    public InetAddress getIp() {
        return ip;
    }

    public boolean checkPlayerCollision(PlayerPawn other) {

        if (this.x == other.getX() && this.y == other.getY() && this.id != other.id && !this.isDead() && !other.isDead()) {
            return true;
        }

        return false;
    }

    class DetonationTask extends TimerTask {

        int x;
        int y;
        PlayerPawn owner;

        public DetonationTask(int x, int y, PlayerPawn own) {
            this.x = x;
            this.y = y;
            this.owner = own;
        }

        @Override
        public void run() {
            // this is the timer that will run when the bomb explodes
            int range = bombsFactory.detonateBomb();
            map.setBombOff(this.x, this.y, owner.getRange());
            System.out.println("bomb Exploded");
            owner.currentBombs--;

        }
    }
}
