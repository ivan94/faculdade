/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.sockets;

import david.ivan.rmi.Data;
import david.ivan.rmi.Processor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Ivan
 */
public class RemoteSocketProcessor extends Processor {

    @Override
    public void process(Data data) {
        if (data instanceof DataPacket) {
            DataPacket dp = (DataPacket) data;
            switch (dp.getRemoteOperation()) {
                case LOOKUP:
                    break;
                case BIND:
                    //this.runBindOperation(dp);
                    break;
                case UNBIND:
                    break;
                case INVOKE:
                    break;
                case ERROR:
                    break;
                case UNKNOWN:
                    break;
                default:
                    throw new AssertionError(dp.getRemoteOperation().name());
            }
        } else {
            throw new RuntimeException("Unsupported data type");
        }
    }
    
    private ObjectInputStream getInputStream(DataPacket data) throws IOException{
        return new ObjectInputStream(new ByteArrayInputStream(data.getData()));
    }
    private ObjectOutputStream getOutputStream() throws IOException{
        return new ObjectOutputStream(new ByteArrayOutputStream());
    }
    
    private void sendErrorPacket(byte[] data){
        
    }

    private void runBindOperation(DataPacket data) throws IOException {
        try {
            ObjectInputStream is = this.getInputStream(data);
            String name = (String) is.readObject();
            String address = data.getAddress();
            
            //Bind name and address to registry
            
            DataPacket resp = new DataPacket(data.getAddress(), (byte)data.getOperation(), new byte[]{}, 0);
            resp.generateChecksum();
            new PacketSender(data.getAddress()).send(resp);
            
            
        } catch (ClassNotFoundException ex) {
            this.sendErrorPacket(new byte[0]);
        }
    }

}
