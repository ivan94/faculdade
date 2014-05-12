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
 * Ouve o endpoint por envio de pacotes.
 * Após ouvir um pacote o mesmo é mandado para o processor associado para processamento e envio de resposta
 * @author ivan
 */
public class SocketListener extends BaseWorker{
    private final String address;
    private Processor processor;

    public SocketListener(String address) throws IOException{
        this.address = address;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public void stop() throws IOException {
        super.stop(); //To change body of generated methods, choose Tools | Templates.
        ListenerManager.removeListener(address);
        SocketManager.closeConnection(address);
    }
    
    
    
    public synchronized DataPacket listen() throws IOException{
        DataInputStream is = new DataInputStream(SocketManager.getConnection(this.address).getInputStream());
        byte op = is.readByte();
        int size = is.readInt();
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = is.readByte();
        }
        int checksum = is.readInt();

        DataPacket pac = new DataPacket(this.address, op, data, checksum);
        if (this.processor != null && this.processor.isRunning()) {
            this.processor.register(pac);
        }
        
        return pac;
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
