package UDPCommunication;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zachcousins
 */
class Sender {
    
    DatagramSocket sendSock;

    public Sender(){
        
        try {
            sendSock = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendCmd(DatagramPacket dp){
        
        //System.out.println("\nSending...\nPlayer: " + dp.getData()[0] + "\tCMD: " + dp.getData()[1]);
        try {
            sendSock.send(dp);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void close(){
        sendSock.disconnect();
        sendSock.close();
    }
    
}
