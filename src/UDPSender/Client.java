package UDPSender;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {
    
    public static void main(String[] argc){
        Player p1 = null;
        try {
            p1 = new Player(1, 5000);
        } catch (SocketException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        p1.start();
    }
}
