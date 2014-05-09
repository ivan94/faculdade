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
 *
 * @author ivan
 */
public class SocketGatekeeper extends BaseWorker{

    private ServerSocket door;
    private int port;

    public SocketGatekeeper() {
        this.port = SocketManager.STANDART_PORT;
    }

    public SocketGatekeeper(int port) {
        this.port = port;
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
            String addr = "rmi://" + s.getInetAddress().getHostName() + ":" + this.port;
            SocketManager.registerConnection(addr, s);
            ListenerManager.getListener(addr).start();
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
