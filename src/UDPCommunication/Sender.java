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
    
    public void sendCmd(DatagramPacket send){
        
        System.out.println("\nSending...\nPlayer: " + send.getData()[0] + "\tCMD: " + send.getData()[1]);
        try {
            sendSock.send(send);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void close(){
        sendSock.close();
    }
    
}
