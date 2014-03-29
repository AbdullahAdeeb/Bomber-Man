package UDPCommunication;

import java.awt.event.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zachcousins
 */
class Receiver implements Runnable {

    DatagramSocket recvSock;
    boolean listen = true;
    boolean newPack = false;
    int port;
    ActionListener actionList;

    public Receiver(ActionListener al, int port) {

        this.actionList = al;
        this.port = port;
        try {
            recvSock = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        System.out.print("Receiver Start\n");
        receiveMessages();
    }

    public void receiveMessages() {

        while (listen == true) {

            byte[] recvData = new byte[50];
            DatagramPacket recvPack = new DatagramPacket(recvData, recvData.length);
            try {
                recvSock.receive(recvPack);
                byte[] data = recvPack.getData();
                String cmd = String.valueOf(data[0]) + String.valueOf(data[1]) + new String(data, "UTF-8").substring(2).trim();
                actionList.actionPerformed(new ActionEvent(this, 0, cmd));

            } catch (IOException ex) {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            }

            // TODO delete those stupid commented code
//            for (int i = 0; i < recvPack.getLength(); i++) {
//                cmd += String.valueOf(data[i]);
//            }



//		            System.out.println(">>>"+data[0]+"="+data[1]+"="+cmd.substring(2).trim());

//            String cmd = String.valueOf(recvPack.getData()[0]) + String.valueOf(recvPack.getData()[2]);

        }
    }

    public void close() {
        recvSock.disconnect();
        recvSock.close();
    }
}
