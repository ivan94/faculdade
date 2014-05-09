/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.sockets;

import david.ivan.rmi.RMIServer;
import david.ivan.rmi.exceptions.RemoteException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Implementação da interface RMIServer para trabalhar com sockets tcp
 * inicia o gatekeeper
 * @author Ivan
 */
public class RMISocketServer implements RMIServer {

    private ArrayList<String> lsConnections;
    private ArrayList<String> nsConnections;
    private SocketGatekeeper nameServer;
    private SocketGatekeeper localServer;

    public RMISocketServer() {
        this.lsConnections = new ArrayList<String>();
        this.nsConnections = new ArrayList<String>();
    }

    public synchronized void registerLSConnection(String address) {
        if (!this.lsConnections.contains(address)) {
            this.lsConnections.add(address);
        }
    }
    public synchronized void registerNSConnection(String address) {
        if (!this.nsConnections.contains(address)) {
            this.nsConnections.add(address);
        }
    }

    @Override
    public void startNameServer() throws RemoteException {
        this.startNameServer(STANDART_PORT);
    }

    @Override
    public void startNameServer(int port) throws RemoteException {
        try {
            this.stopNameServer();
            //TODO criar o processor
            this.nameServer = new SocketGatekeeper(port, null, this);
            this.nameServer.start();
        } catch (IOException ex) {
            throw new RemoteException(ex);
        }
    }

    @Override
    public void stopNameServer() throws RemoteException {
        try {
            if (this.nameServer != null && this.nameServer.isRunning()) {
                this.nameServer.stop();
                for (String addr : this.nsConnections) {
                    ListenerManager.closeListener(addr);
                    SocketManager.closeConnection(addr);
                }
            }
        } catch (IOException ex) {
            throw new RemoteException(ex);
        }
    }

    @Override
    public void startLocalServer() throws RemoteException {
        this.startLocalServer(STANDART_PORT);
    }

    @Override
    public void startLocalServer(int port) throws RemoteException {
        try {
            this.stopLocalServer();
            //TODO criar o processor
            this.localServer = new SocketGatekeeper(port, null, this);
            this.localServer.start();
        } catch (IOException ex) {
            throw new RemoteException(ex);
        }
    }

    @Override
    public void stopLocalServer() throws RemoteException {
        try {
            if (this.localServer != null && this.localServer.isRunning()) {
                this.localServer.stop();
                for (String addr : this.lsConnections) {
                    ListenerManager.closeListener(addr);
                    SocketManager.closeConnection(addr);
                }
            }
        } catch (IOException ex) {
            throw new RemoteException(ex);
        }
    }

}
