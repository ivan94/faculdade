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

/**
 *
 * @author ivan
 */
public class SocketManager {
    public static final int STANDART_PORT = 572;
    
    private static final HashMap<String, Socket> connections = new HashMap<String, Socket>();
    
    public synchronized static Socket getConnection(String address) throws MalformedURLException, IOException{
        Socket s = connections.get(address);
        if(s == null){
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

    private static URI parseURL(String url) throws MalformedURLException {
        try{
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if(host == null){
                throw new MalformedURLException();
            }else{
                if(scheme != null){
                    if(scheme.equals("rmi")){
                        return uri;
                    }else{
                        throw new MalformedURLException();
                    }
                }else{
                    return new URI("rmi://"+host);
                }
            }
        }catch(URISyntaxException ex){
            throw new MalformedURLException();
        }
        
    }
}
