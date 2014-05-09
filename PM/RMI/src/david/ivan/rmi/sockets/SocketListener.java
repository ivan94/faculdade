/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import david.ivan.rmi.BaseWorker;
import david.ivan.rmi.Processor;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ivan
 */
public class SocketListener extends BaseWorker{
    private final String address;
    private PacketProcessor

    public SocketListener(String address) throws IOException{
        this.address = address;
    }
    
    public synchronized void listen() throws IOException{
        DataInputStream is = new DataInputStream(SocketManager.getConnection(this.address).getInputStream());
        byte op = is.readByte();
        int size = is.readInt();
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = is.readByte();
        }
        int checksum = is.readInt();

        DataPacket pac = new DataPacket(op, data, checksum);

        PacketProcessor ps = PacketProcessor.getProcessor();
        if (ps != null) {
            ps.register(pac);
        } else {
            throw new IOException();
        }
    }
    
    @Override
    protected boolean doWork() {
        try {
            this.listen();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(SocketListener.class.getName()).log(Level.INFO, "LOG", ex);
            return false;
        }
        
    }
}
