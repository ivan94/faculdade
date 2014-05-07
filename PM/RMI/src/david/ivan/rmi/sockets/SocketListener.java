/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
/**
 *
 * @author ivan
 */
public class SocketListener extends BaseListener{
    private final Socket socket;
    private final String address;

    public SocketListener(String address) throws IOException{
        this.address = address;
        this.socket = SocketManager.getConnection(address);
        if(this.socket.isClosed()){
            throw new IOException();
        }
    }
    
    @Override
    protected void listen() throws IOException {
        DataInputStream is = (DataInputStream) this.socket.getInputStream();
        byte op = is.readByte();
        int size = is.readInt();
        byte[] data = new byte[size];
        for(int i = 0; i<size; i++){
            data[i] = is.readByte();
        }
        int checksum = is.readInt();
        
        DataPacket pac = new DataPacket(op, data, checksum);
        
        PacketProcessor.getProcessor().register(pac);
        
    }
}
