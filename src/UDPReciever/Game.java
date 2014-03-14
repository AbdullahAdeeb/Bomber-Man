package UDPReciever;

import UDPReciever.Communication;
import java.awt.event.*;
import java.io.IOException;

public class Game {
    
    Communication com;
    
    public Game() throws IOException, Exception{
        
       com = new Communication(new IncomingActionListener());
    }
    
    class IncomingActionListener implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent e){
            
            String actionCmd = e.getActionCommand();
            int p = Character.getNumericValue(actionCmd.charAt(0));
            int c = Character.getNumericValue(actionCmd.charAt(1));
            System.out.println("--RECEIVED CMD--\nPlayer: " + p + "\nCMD: " + c + "\n");
        }
    }
    
    public static void main(String[] args) throws IOException, Exception {
        
        Game bomber = new Game();
    }
}
