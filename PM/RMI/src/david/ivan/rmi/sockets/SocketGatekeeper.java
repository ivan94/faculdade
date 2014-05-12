/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.sockets;

import david.ivan.rmi.BaseWorker;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Worker que monitora a porta especificada e aceita conexões
 * @author ivan
 */
public class SocketGatekeeper extends BaseWorker{

    private ServerSocket door;
    private int port;
    private RMISocketServer server;

    public SocketGatekeeper( RMISocketServer server) {
        this.port = 0;
        this.server = server;
    }

    public SocketGatekeeper(int port, RMISocketServer server) {
        this.port = port;
        this.server = server;
    }

    public int getPort() {
        if(this.door != null){
            return this.door.getLocalPort();
        }else{
            return this.port;
        }
    }
    
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void start() throws IOException {
        this.door = new ServerSocket(this.port);
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        try {
            this.door.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketGatekeeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    @Override
    public boolean doWork() {
        try{
            Socket s = this.door.accept();
            String addr = "rmi://" + s.getInetAddress().getHostName() + ":" + s.getPort();
            SocketManager.registerConnection(addr, s);
            SocketListener l = ListenerManager.getListener(addr);
            l.setProcessor(this.server.getProcessor());
            l.start();
            return true;
        }catch(SocketException ex){
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "LOG:", ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(SocketGatekeeper.class.getName()).log(Level.INFO, "LOG:", ex);
            return false;
        }
    }
}
