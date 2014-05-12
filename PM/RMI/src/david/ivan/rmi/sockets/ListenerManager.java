/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import java.io.IOException;
import java.util.HashMap;

/**
 * Gerenciador de listeners de soquete. Associa o listener ao endereço do endpoint que está ouvindo
 * É usada principalmente para fechar os listeners no processo de finalização da aplicação
 * @author ivan
 */
public class ListenerManager {
    private static final HashMap<String, SocketListener> listeners = new HashMap<String, SocketListener>();
    
    /**
     * Procura o listener associado ao endereço informado
     * Se o listener não existir é criado um novo, mas sem iniciar sua execução e sem um processor associado
     * @param address
     * @return
     * @throws IOException 
     */
    public synchronized static SocketListener getListener(String address) throws IOException{
        SocketListener listener = listeners.get(address);
        if(listener == null){
            listener = new SocketListener(address);
            listeners.put(address, listener);
        }
        return listener;
    }
    
    public synchronized static void closeListener(String address) throws IOException{
        SocketListener l = listeners.remove(address);
        if(l != null && l.isRunning()){
            l.stop();
        }
    }
    public synchronized static void closeAllListeners() throws IOException{
        for(String k : listeners.keySet()){
            closeListener(k);
        }
    }
    
    public synchronized static void removeListener(String address){
        listeners.remove(address);
    }
}
