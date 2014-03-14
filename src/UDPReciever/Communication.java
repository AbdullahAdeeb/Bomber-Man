package UDPReciever;

import java.awt.event.*;
import java.net.*;

public class Communication {
    
    InetAddress player;
    int recvPort = 5000;
    
    DatagramPacket recvPack, newPack;
    boolean newPacket = false;
    Receiver recv;
    
    ActionListener al;
    
    public Communication(ActionListener al){
        
        this.al = al;
        init();
    }
    
    public void init(){
        
        recv = new Receiver(al, recvPort);
        new Thread(recv).start();
        
    }
}
