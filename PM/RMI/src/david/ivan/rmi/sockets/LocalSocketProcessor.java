/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.sockets;

import david.ivan.rmi.Data;
import david.ivan.rmi.Processor;
import david.ivan.rmi.RemoteOperation;
import david.ivan.rmi.exceptions.RemoteException;
import david.ivan.rmi.registry.RegistryReference;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
public class LocalSocketProcessor extends Processor {

    RMISocketLocalServer server;

    public LocalSocketProcessor(RMISocketLocalServer server) {
        this.server = server;
    }

    @Override
    public void process(Data data) {
        if (data instanceof DataPacket) {
            DataPacket dp = (DataPacket) data;
            if (dp.isCheckSumValid()) {
                switch (dp.getRemoteOperation()) {
                    case INVOKE:
                        this.runInvokeOperation(dp);
                        break;
                    case GETTYPE:
                        this.runGetTypeOperation(dp);
                        break;
                    default:
                        this.sendUnknownPacket(dp.getAddress());
                        break;
                }
            } else {
                this.sendErrorPacket(dp.getAddress(), new byte[0]);
            }
        } else {
            throw new RuntimeException("Unsupported data type");
        }
    }

    private ObjectInputStream getInputStream(DataPacket data) throws IOException {
        return new ObjectInputStream(new ByteArrayInputStream(data.getData()));
    }

    private void sendUnknownPacket(String address) {
        try {
            DataPacket p = new DataPacket(null, (byte) RemoteOperation.UNKNOWN.getVal(), new byte[0]);
            p.generateChecksum();
            new PacketSender(address).send(p);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            throw new RuntimeException("Connection problem at " + address, ex);
        }
    }

    private void sendErrorPacket(String address, byte[] data) {
        try {
            DataPacket p = new DataPacket(null, (byte) RemoteOperation.ERROR.getVal(), data);
            p.generateChecksum();
            new PacketSender(address).send(p);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            throw new RuntimeException("Connection problem at " + address, ex);
        }
    }

    private void runInvokeOperation(DataPacket data) {
        try {
            ObjectInputStream is = this.getInputStream(data);
            String name = (String) is.readObject();
            String method = (String) is.readObject();
            int argsN = (Integer)is.readObject();
            Object[] args = new Object[argsN];
            for (int i = 0; i < argsN; i++) {
                args[i] = is.readObject();
            }

            Object ret = ((RegistryReference) this.server.getRegistry()).invoke(name, method, args);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(baos);

            os.writeObject(ret);

            DataPacket resp = new DataPacket(data.getAddress(), (byte) data.getOperation(), baos.toByteArray());
            new PacketSender(data.getAddress()).send(resp);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (RemoteException ex) {
            this.sendErrorPacket(data.getAddress(), new byte[]{(byte) data.getOperation()});
        }
    }
    
    private void runGetTypeOperation(DataPacket data) {
        try {
            ObjectInputStream is = this.getInputStream(data);
            String name = (String) is.readObject();

            String ret = ((RegistryReference) this.server.getRegistry()).getRemoteType(name);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(baos);

            os.writeObject(ret);

            DataPacket resp = new DataPacket(data.getAddress(), (byte) data.getOperation(), baos.toByteArray());
            new PacketSender(data.getAddress()).send(resp);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            this.sendErrorPacket(data.getAddress(), new byte[0]);
        } catch (RemoteException ex) {
            this.sendErrorPacket(data.getAddress(), new byte[]{(byte) data.getOperation()});
        }
    }

}
