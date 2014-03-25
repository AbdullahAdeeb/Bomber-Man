package UDPCommunication;

import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zachcousins
 */
class Receiver implements Runnable{
  
    DatagramPacket recvPack;
    DatagramSocket recvSock;
    boolean listen = true;
    boolean newPack = false;
    int port;
    
    ActionListener actionList;
    
    public Receiver(ActionListener al, int port){
        
        this.actionList = al;
        this.port = port;
        try {
            recvSock = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void run(){
        System.out.print("Receiver Start\n");
        receiveMessages(); 
    }
    
    public void receiveMessages(){
        
        while(listen == true){
            
            byte[] recvData = new byte[2];
            recvPack = new DatagramPacket(recvData, recvData.length);
            try {
                recvSock.receive(recvPack);
            } catch (IOException ex) {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            String cmd = String.valueOf(getCmd(recvPack, 0)) + String.valueOf(getCmd(recvPack, 1));
            actionList.actionPerformed(new ActionEvent(this, 0, cmd)); 
        }
    }
    
    private int getCmd(DatagramPacket cmd, int i){
        return cmd.getData()[i];
    }
    
    public void close(){
        recvSock.disconnect();
        recvSock.close();
    }
}
