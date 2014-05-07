/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author ivan
 */
public class PacketSender {
    private final Socket socket;

    public PacketSender(String address) throws IOException {
        this.socket = SocketManager.getConnection(address);
    }
    
    public void send(DataPacket packet) throws IOException{
        DataOutputStream os = (DataOutputStream)this.socket.getOutputStream();
        int op = packet.getOperation();
        byte[] data = packet.getData();
        int size = data.length;
        int checksum = packet.getChecksum();
        
        os.writeByte(op);
        os.writeInt(size);
        os.write(data);
        os.writeInt(checksum);
        
    }
    
    
}
