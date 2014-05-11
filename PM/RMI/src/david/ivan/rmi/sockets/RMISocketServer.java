/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.sockets;

import david.ivan.rmi.Processor;
import david.ivan.rmi.RMIServer;
import david.ivan.rmi.exceptions.RemoteException;
import david.ivan.rmi.registry.Registry;
import david.ivan.rmi.registry.RegistryImpl;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Implementação da interface RMIServer para trabalhar com sockets tcp
 * inicia o gatekeeper
 * @author Ivan
 */
public abstract class RMISocketServer implements RMIServer {
    private ArrayList<String> connections;
    private SocketGatekeeper gatekeeper;

    public RMISocketServer() {
        this.connections = new ArrayList<String>();
    }

    public synchronized void registerConnection(String address) {
        if (!this.connections.contains(address)) {
            this.connections.add(address);
        }
    }

    public abstract Registry getRegistry();

    public abstract Processor getProcessor();

    @Override
    public void start() throws RemoteException {
        this.start(STANDART_PORT);
    }

    @Override
    public void start(int port) throws RemoteException {
        try {
            this.stop();
            Processor p = this.getProcessor();
            if(!p.isRunning()) p.start();
            this.gatekeeper = new SocketGatekeeper(port, this);
            this.gatekeeper.start();
        } catch (IOException ex) {
            throw new RemoteException(ex);
        }
    }

    @Override
    public void stop() throws RemoteException {
        try {
            if (this.gatekeeper != null && this.gatekeeper.isRunning()) {
                this.gatekeeper.stop();
                for (String addr : this.connections) {
                    ListenerManager.closeListener(addr);
                    SocketManager.closeConnection(addr);
                }
            }
            if(this.getProcessor().isRunning())
                this.getProcessor().stop();
        } catch (IOException ex) {
            throw new RemoteException(ex);
        }finally{
            this.getProcessor().stop();
        }
    }
}
