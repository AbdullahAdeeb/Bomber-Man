package UDPCommunication;

import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Server {
    
    Communication com;
    ArrayList players;
    
    public Server() throws IOException, Exception{
        this.players = new ArrayList();
        com = new Communication(new IncomingActionListener(), 0);
    }
    
    class IncomingActionListener implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent e){
            
            String actionCmd = e.getActionCommand();
            int p = Character.getNumericValue(actionCmd.charAt(0));
            int c = Character.getNumericValue(actionCmd.charAt(1));
            
            if(players.contains(p)){
                System.out.println("--RECEIVED CMD--\nPlayer: " + p + "\nCMD: " + c + "\n");
            }else{
                players.add(p);
                System.out.println("--RECEIVED CMD--\nNew Player: " + p + "\n");
            }
            if(c == 6){
                players.remove((Object)p);
                System.out.println("--RECEIVED CMD--\nPlayer Left: " + p + "\n");
            }else if(c == 7){
                System.out.println(players);
            }
        }
    }
}
