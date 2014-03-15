

package UDPCommunication;

import java.net.*;
import java.util.*;

public class Client extends Thread{

    final int ID;
    Communication com;
    
    public Client(int ID) throws SocketException {
        this.ID = ID;
        com = new Communication(null, 1);
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
            System.out.println(text);
            int cmd = getCmd(text);
            if(cmd != 0){
                com.send(ID, cmd);
            }    
        }
    }
    
    @Override
    public void run(){
        System.out.print("Player Start\n");
        playerAction();
        
    }
}

