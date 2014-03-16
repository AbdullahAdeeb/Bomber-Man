package UDPCommunication;

import java.awt.event.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zachcousins
 */
public class Connection {
    
    InetAddress player;
    
    DatagramPacket sendPack;
    DatagramSocket recvSock, sendSock;
    Receiver recv;
    Sender sender;
    
    ActionListener al;  //TODO get the inet address from the client at the first rcv
    
    public Connection(ActionListener al){
        try {
            player = InetAddress.getLocalHost();
            this.al = al;
            init();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void init(){
        
            recv = new Receiver(al, 5000);
            new Thread(recv).start();
            sender = new Sender();

    }
    
    public void sendID(int id){
        
        byte[] cmd = new byte[2];
        cmd[0] = (byte)id;
        cmd[1] = (byte)0;
        sendPack = new DatagramPacket(cmd, cmd.length, player, 8000);
        sender.sendCmd(sendPack);
    }
    
    public void send(int id, int c){
        
        byte[] cmd = new byte[2];
        cmd[0] = (byte)id;
        cmd[1] = (byte)c;
        sendPack = new DatagramPacket(cmd, cmd.length, player, 6000+id);
        sender.sendCmd(sendPack);
    }
    
    public void close(){
        recv.close();
        sender.close();
    }
}
