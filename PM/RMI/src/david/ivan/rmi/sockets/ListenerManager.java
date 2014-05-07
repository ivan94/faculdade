/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author ivan
 */
public class ListenerManager {
    private static final HashMap<String, SocketListener> listeners = new HashMap<String, SocketListener>();
    
    public synchronized static SocketListener getListener(String address) throws IOException{
        SocketListener listener = listeners.get(address);
        if(listener == null){
            listener = new SocketListener(address);
            listeners.put(address, listener);
        }
        return listener;
    }
    
    public synchronized static void closeListener(String address){
        SocketListener l = listeners.remove(address);
        if(l != null && l.isRunning()){
            l.stop();
        }
    }
    public synchronized static void closeAllListeners(){
        for(String k : listeners.keySet()){
            closeListener(k);
        }
    }
}
