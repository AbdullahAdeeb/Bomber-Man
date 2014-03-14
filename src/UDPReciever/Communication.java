package UDPReciever;

import java.awt.event.*;
import java.io.IOException;
import java.net.*;

public class ConcurrentServer {
    
    int recvPort = 5000;
    ServerSocket server;
    ActionListener al;
    
    public ConcurrentServer(ActionListener al) throws IOException{
        
        this.al = al;
        server = new ServerSocket(recvPort);
        init();
    }
    
    public void init(){
        
        while(true){
            Socket sock = server.accept();
            
        }
    }
}
