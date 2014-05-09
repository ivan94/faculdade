/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gerenciador de sockets abertos
 * Todos sockets abertos devem ser registrados no SocketManager
 * Funcionamento similar ao ListenerManager
 * @author ivan
 */
public class SocketManager {
    public static final int STANDART_PORT = 572;
    
    private static final HashMap<String, Socket> connections = new HashMap<String, Socket>();
    
    public synchronized static Socket getConnection(String address) throws MalformedURLException, IOException{
        Socket s = connections.get(address);
        if(s == null || s.isClosed()){
            URI addr = parseURL(address);
            int port = addr.getPort();
            if(port != -1){
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
        Socket old = connections.put(address, socket);
        if(old != null && !old.isClosed()){
            try {
                old.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    private static URI parseURL(String url) throws MalformedURLException {
        try{
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if(host == null){
                throw new MalformedURLException("No host specified");
            }else{
                if(scheme != null && !scheme.equals("rmi")){
                    throw new MalformedURLException("Unsupported protocol");
                }else{
                    return uri;
                }
            }
        }catch(URISyntaxException ex){
            throw new MalformedURLException();
        }
        
    }
}
