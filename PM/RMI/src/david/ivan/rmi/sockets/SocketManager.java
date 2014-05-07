/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

/**
 *
 * @author ivan
 */
public class SocketManager {
    public static final int STANDART_PORT = 572;
    
    private static final HashMap<String, Socket> connections = new HashMap<String, Socket>();
    private static final HashMap<String, SocketListener> listeners = new HashMap<String, SocketListener>();
    
    public synchronized static Socket getConnection(String address) throws MalformedURLException, IOException{
        Socket s = connections.get(address);
        if(s == null){
            URL addr = new URL(address);
            int port = addr.getPort();
            if(port == -1){
                s = new Socket(addr.getHost(), addr.getPort());
            }else{
                s = new Socket(addr.getHost(), STANDART_PORT);
            }
            connections.put(address, s);
        }
        return s;
    }
    
    public synchronized static void registerConnection(String address, Socket socket){
        if(socket == null){
            throw new NullPointerException();
        }
        if(!connections.containsKey(address)){
            connections.put(address, socket);
        }
    }
    
    public synchronized static void closeConnection(String address) throws IOException{
        Socket s = connections.remove(address);
        if(s != null){
            s.close();
        }
    }
    public synchronized static void closeAllConnections() throws IOException{
        for(String k: connections.keySet()){
            closeConnection(k);
        }
    }
    
    public synchronized static void putListener(String address, SocketListener listener){
        listeners.put(address, listener);
    }
}
