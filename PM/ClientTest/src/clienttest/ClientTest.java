/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clienttest;

import david.ivan.rmi.sockets.DataPacket;
import david.ivan.rmi.sockets.PacketSender;
import david.ivan.rmi.sockets.SocketManager;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 *
 * @author Ivan
 */
public class ClientTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        PacketSender sender = new PacketSender("//localhost:8080");
        sender.send(new DataPacket((byte)1, new byte[]{2,3,4,5}, 6));
        
        BufferedInputStream bf = new BufferedInputStream(System.in);
        bf.read();
        
        SocketManager.closeAllConnections();
    }
    
}
