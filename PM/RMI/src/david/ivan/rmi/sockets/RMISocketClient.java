/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import david.ivan.rmi.RMIClient;
import david.ivan.rmi.RemoteOperation;
import david.ivan.rmi.exceptions.RemoteException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author administracao
 */
public class RMISocketClient implements RMIClient{
    
    private byte[] toByteArray(Object... args) throws IOException{
        ByteArrayOutputStream baos  = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(baos);
        for(Object arg : args){
            os.writeObject(arg);
        }
        return baos.toByteArray();
    }
    
    @Override
    public void bind(String address, String name, int port) throws RemoteException{
        try {
            PacketSender sender = new PacketSender(address);
            SocketListener l = new SocketListener(address);
            
            DataPacket data = new DataPacket(address, (byte)RemoteOperation.BIND.getVal(), this.toByteArray(name, port));
            sender.send(data);
            
            data = l.listen();
            if(!data.isCheckSumValid() || data.getRemoteOperation() != RemoteOperation.BIND){
                throw new RemoteException();
            }
            
        } catch (IOException ex) {
            throw new RemoteException(ex);
        }
    }
    
    @Override
    public void unbind(String address, String name) throws RemoteException{
        try {
            PacketSender sender = new PacketSender(address);
            SocketListener l = new SocketListener(address);
            
            DataPacket data = new DataPacket(address, (byte)RemoteOperation.UNBIND.getVal(), this.toByteArray(name));
            sender.send(data);
            
            data = l.listen();
            if(!data.isCheckSumValid() || data.getRemoteOperation() != RemoteOperation.UNBIND){
                throw new RemoteException();
            }
            
        } catch (IOException ex) {
            throw new RemoteException(ex);
        }
    }
    
    @Override
    public String lookup(String address, String name) throws RemoteException{
        try {
            PacketSender sender = new PacketSender(address);
            SocketListener l = new SocketListener(address);
            
            DataPacket data = new DataPacket(address, (byte)RemoteOperation.LOOKUP.getVal(), this.toByteArray(name));
            sender.send(data);
            
            data = l.listen();
            if(!data.isCheckSumValid() || data.getRemoteOperation() != RemoteOperation.LOOKUP){
                throw new RemoteException();
            }
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data.getData());
            ObjectInputStream is = new ObjectInputStream(bais);
            return (String) is.readObject();
            
        } catch (IOException ex) {
            throw new RemoteException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RemoteException(ex);
        }
    }
    
    @Override
    public Object invoke(String address, String name, String method, Object[] args) throws RemoteException{
        try {
            PacketSender sender = new PacketSender(address);
            SocketListener l = new SocketListener(address);
            
            Object[] payload = new Object[args.length+3];
            payload[0] = name;
            payload[1] = method;
            payload[2] = (Integer)args.length;
            for(int i =3; i<payload.length; i++){
                payload[i] = args[i-3];
            }
            
            sender.send(new DataPacket(address, (byte)RemoteOperation.INVOKE.getVal(), this.toByteArray(payload)));
            DataPacket data = l.listen();
            
            if(!data.isCheckSumValid() || data.getRemoteOperation() != RemoteOperation.INVOKE){
                throw new RemoteException();
            }
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data.getData());
            ObjectInputStream is = new ObjectInputStream(bais);
            
            return is.readObject();
        } catch (IOException ex) {
            throw new RemoteException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RemoteException(ex);
        }
    }
    
    @Override
    public Class getRemoteType(String address, String name) throws RemoteException{
        try {
            PacketSender sender = new PacketSender(address);
            SocketListener l = new SocketListener(address);
            
            byte[] d1 = this.toByteArray(name);
            
            sender.send(new DataPacket(address, (byte)RemoteOperation.GETTYPE.getVal(), d1));
            DataPacket data = l.listen();
            
            if(!data.isCheckSumValid() || data.getRemoteOperation() != RemoteOperation.GETTYPE){
                throw new RemoteException();
            }
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data.getData());
            ObjectInputStream is = new ObjectInputStream(bais);
            
            String className = (String) is.readObject();
            
            return Class.forName(className);
            
        } catch (IOException ex) {
            throw new RemoteException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RemoteException(ex);
        }
    }
    
    @Override
    public void closeConnection(String address) throws RemoteException{
        try {
            SocketManager.closeConnection(address);
        } catch (IOException ex) {
            throw new RemoteException(ex);
        }
    }
    
}
