package UDPCommunication;

import java.awt.event.*;
import java.io.IOException;

public class Server {
    
    Communication com;
    
    public Server() throws IOException, Exception{
        
       com = new Communication(new IncomingActionListener(), 0);
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
}
