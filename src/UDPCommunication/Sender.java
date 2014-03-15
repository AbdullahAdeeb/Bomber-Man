package UDPCommunication;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender {
    
    DatagramSocket sendSock;
    int port;

    public Sender(){

        try {
            sendSock = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendCmd(DatagramPacket send){
        
        System.out.println("Sending...\nPlayer: " + send.getData()[0] + "\tCmd: " + send.getData()[1]);
        try {
            sendSock.send(send);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
