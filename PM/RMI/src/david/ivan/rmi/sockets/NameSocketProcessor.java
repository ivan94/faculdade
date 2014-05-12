/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.sockets;

import david.ivan.rmi.Data;
import david.ivan.rmi.Processor;
import david.ivan.rmi.RemoteAddress;
import david.ivan.rmi.RemoteOperation;
import david.ivan.rmi.exceptions.RemoteException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Processor para o serviço de nomes
 * Responde a pacotes de bind unbind e lookup
 * Qualquer outro pacote recebido resulta em um pacote de operação UNKNOWN sendo enviado de volta
 * @author Ivan
 */
public class NameSocketProcessor extends Processor {
    RMISocketNameServer server;
    
    public NameSocketProcessor(RMISocketNameServer server) {
        this.server = server;
    }
    
    
    
    @Override
    public void process(Data data) {
        if (data instanceof DataPacket) {
            DataPacket dp = (DataPacket) data;
            if (dp.isCheckSumValid()) {
                switch (dp.getRemoteOperation()) {
                    case LOOKUP:
                        this.runLookupOperation(dp);
                        break;
                    case BIND:
                        this.runBindOperation(dp);
                        break;
                    case UNBIND:
                        this.runUnbindOperation(dp);
                        break;
                    default:
                        this.sendUnknownPacket(dp.getAddress());
                        break;
                }
            }else{
                this.sendErrorPacket(dp.getAddress(), new byte[0]);
            }
        } else {
            throw new RuntimeException("Unsupported data type");
        }
    }
    
    private ObjectInputStream getInputStream(DataPacket data) throws IOException{
        return new ObjectInputStream(new ByteArrayInputStream(data.getData()));
    }
    
    private void sendUnknownPacket(String address){
        try {
            DataPacket p = new DataPacket(null, (byte)RemoteOperation.UNKNOWN.getVal(), new byte[0]);
            p.generateChecksum();
            new PacketSender(address).send(p);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            throw new RuntimeException("Connection problem at "+address, ex);
        }
    }
    
    private void sendErrorPacket(String address, byte[] data){
        try {
            DataPacket p = new DataPacket(null, (byte)RemoteOperation.ERROR.getVal(), data);
            p.generateChecksum();
            new PacketSender(address).send(p);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            throw new RuntimeException("Connection problem at "+address, ex);
        }
    }

    private void runBindOperation(DataPacket data){
        try {
            ObjectInputStream is = this.getInputStream(data);
            String name = (String) is.readObject();
            int port = (Integer)is.readObject();
            String address = data.getAddress();
            URI uri = new URI(address);
            
            this.server.getRegistry().bind(name, new RemoteAddress("rmi://"+uri.getHost()+":"+port, null));
            
            DataPacket resp = new DataPacket(data.getAddress(), (byte)data.getOperation(), new byte[]{});
            resp.generateChecksum();
            new PacketSender(data.getAddress()).send(resp);
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (RemoteException ex) {
            this.sendErrorPacket(data.getAddress(), new byte[]{(byte)data.getOperation()});
        } catch (URISyntaxException ex) {
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } 
    }
    
    private void runUnbindOperation(DataPacket data){
        try {
            ObjectInputStream is = this.getInputStream(data);
            String name = (String) is.readObject();
            
            this.server.getRegistry().unbind(name);
            
            DataPacket resp = new DataPacket(data.getAddress(), (byte)data.getOperation(), new byte[]{});
            resp.generateChecksum();
            new PacketSender(data.getAddress()).send(resp);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (RemoteException ex) {
            this.sendErrorPacket(data.getAddress(), new byte[]{(byte)data.getOperation()});
        } 
    }
    
    private void runLookupOperation(DataPacket data){
        try {
            ObjectInputStream is = this.getInputStream(data);
            String name = (String) is.readObject();
            
            String address = ((RemoteAddress)this.server.getRegistry().lookup(name)).getAddress();
                       
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(baos);
            os.writeObject(address);
            
            DataPacket resp = new DataPacket(data.getAddress(), (byte)data.getOperation(), baos.toByteArray());
            resp.generateChecksum();
            new PacketSender(data.getAddress()).send(resp);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (RemoteException ex) {
            this.sendErrorPacket(data.getAddress(), new byte[]{(byte)data.getOperation()});
        }
    }

}
