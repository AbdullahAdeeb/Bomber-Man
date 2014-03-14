package UDPSender;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player extends Thread{

    final int ID;
    Sender com;
    
    public Player(int ID, int port) throws SocketException {
        this.ID = ID;
        com = new Sender(port);
    }
    
    /*  1 - UP
     *  2 - DOWN 
     *  3 - LEFT
     *  4 - RIGHT
     *  5 - Drop Bomb
     */
    
    private int getCmd(String cmd){
        
        int play;
        switch (cmd) {
            case "up":
                play = 1;
                break;
            case "down":
                play = 2;
                break;
            case "left":
                play = 3;
                break;
            case "right":
                play = 4;
                break;
            case "drop":
                play = 5;
                break;
            default:
                play = 0;
                break;
        }
        return play;
    }
    
    public void playerAction(){
        
        while(true){
            Scanner scan = new Scanner(System.in);
            String text = scan.nextLine();
            int cmd = getCmd(text);
            if(cmd != 0){
                try {
                    com.send(ID, cmd);
                } catch (IOException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
            }    
        }

    }
    
    @Override
    public void run(){
        System.out.print("Player Start\n");
        playerAction();
        
    }
}
