/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author ivan
 */
public class SocketGatekeeper implements Runnable{
    private ServerSocket door;
    private Thread gatekeeper;
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
    
    public void start() throws IOException{
        this.door = new ServerSocket(this.port);
        this.gatekeeper = new Thread(this);
        this.gatekeeper.start();
    }
    
    public void stop(){
        
    }
    
    public void waitForConnection(){
        
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
