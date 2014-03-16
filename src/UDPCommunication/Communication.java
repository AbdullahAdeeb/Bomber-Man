package UDPCommunication;

import java.awt.event.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zachcousins
 */
public class Communication {
    
    InetAddress player;
    
    DatagramPacket recvPack, sendPack;
    DatagramSocket recvSock, sendSock;
    boolean newPacket = false;
    Receiver recv;
    Sender send;
    int sc;
    
    ActionListener al;
    
    public Communication(ActionListener al, int sc){
        try {
            player = InetAddress.getLocalHost();
            this.al = al;
            this.sc = sc;
            init();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void init(){
        
        if(sc == 0){
            recv = new Receiver(al, 5000);
            new Thread(recv).start();
            send = new Sender();
        }else{
            recv = new Receiver(al, 6000+sc);
            new Thread(recv).start();
            send = new Sender();
        }
    }
    
    public void send(int p, int c){
        
        byte[] cmd = new byte[2];
        cmd[0] = (byte)p;
        cmd[1] = (byte)c;
        if(sc == 0){
            sendPack = new DatagramPacket(cmd, cmd.length, player, 6000+p);
        }else{
            sendPack = new DatagramPacket(cmd, cmd.length, player, 5000);
        }
        send.sendCmd(sendPack);
    }
    
    public void close(){
        recv.close();
        send.close();
    }
}
