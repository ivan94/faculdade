/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servertest;

import david.ivan.rmi.sockets.DataPacket;
import david.ivan.rmi.sockets.PacketProcessor;
import david.ivan.rmi.sockets.SocketGatekeeper;
import david.ivan.rmi.sockets.SocketManager;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 *
 * @author Ivan
 */
public class ServerTest {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        PacketProcessor proc = new PacketProcessor() {

            @Override
            public void process(DataPacket packet) {
                System.out.print(packet.getOperation() + " " + packet.getData().length);
                for(byte b: packet.getData()){
                    System.out.print(" "+b);
                }
                System.out.println(" "+packet.getChecksum());
            }
        };
        
        proc.start();
        
        PacketProcessor.setProcessor(proc);
        
        SocketGatekeeper gk = new SocketGatekeeper(8080);
        gk.start();
        
        BufferedInputStream bf = new BufferedInputStream(System.in);
        bf.read();
        
        gk.stop();
        SocketManager.closeAllConnections();
        proc.stop();
    }
    
}
