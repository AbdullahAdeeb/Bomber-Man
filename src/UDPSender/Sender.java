package UDPSender;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender {
    
    DatagramSocket sendSock;
    InetAddress ip;
    int port;

    public Sender(int port){
        this.port = port;
        try {
            this.ip = InetAddress.getByName("localhost");
            this.sendSock = new DatagramSocket(port+1, ip);
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String getCmd(int cmd){
        
        String play = null;
        
        if(cmd == 1) {
            play = "UP";
        }else if(cmd == 2){
            play = "DOWN";
        }else if(cmd == 3){
            play = "LEFT";
        }else if(cmd == 4){
            play = "RIGHT";
        }else if(cmd == 5){
            play = "DROP";
        }else{
            play = "ERROR";
        }
        return play;
    }
    
    public void send(Integer p, Integer cmd) throws IOException{
        
        byte[] data = new byte[2];
        
        data[0] = p.byteValue();
        data[1] = cmd.byteValue();
        
        DatagramPacket send = new DatagramPacket(data, data.length, ip, port);
        
        sendSock.send(send);
       
        System.out.print("Send cmd: " + cmd + "\tFrom ID: " + p + "\n");
    }
    
}
