package UDPCommunication;

import java.io.IOException;

public class ComTest {
    
    public static void main(String[] args) throws IOException, Exception {
        
        Server bomber = new Server();
        
        Thread.sleep(2000);
        
        Client p1 = new Client(1);
        Client p2 = new Client(2);
       
        p1.start();
        p2.start();
    }
    
}
