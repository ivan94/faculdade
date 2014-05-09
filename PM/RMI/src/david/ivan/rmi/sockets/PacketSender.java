/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe que abstrai o envio de pacotes ao endereço informado
 * @author ivan
 */
public class PacketSender {
    private final String address;

    public PacketSender(String address) throws IOException {
        this.address = address;
    }
    
    public void send(DataPacket packet) throws IOException{
        DataOutputStream os = new DataOutputStream(SocketManager.getConnection(address).getOutputStream());
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
