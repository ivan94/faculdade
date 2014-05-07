/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author ivan
 */
public class SocketGatekeeper extends BaseListener{

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
    }

    @Override
    public void listen() throws IOException {
        Socket s = this.door.accept();
        String addr = "rmi://" + s.getInetAddress().getHostName() + "/" + this.port;
        SocketManager.registerConnection(addr, s);
        //TODO: start listener for this socket
    }
}
