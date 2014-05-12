/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.registry;

import david.ivan.rmi.RMIServer;
import david.ivan.rmi.exceptions.RemoteException;
import david.ivan.rmi.sockets.ListenerManager;
import david.ivan.rmi.sockets.RMISocketClient;
import david.ivan.rmi.sockets.RMISocketLocalServer;
import david.ivan.rmi.sockets.RMISocketNameServer;
import david.ivan.rmi.sockets.SocketManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
public class LocateRegistry {
    public static final int STANDART_PORT = 572;
    
    private static final HashMap<String, Registry> registries = new HashMap<String, Registry>();
    private static RMIServer server;
    
    public static Registry getRegistry(String host){
        return getRegistry(host, STANDART_PORT);
    }
    public static Registry getRegistry(String host, int port){
        String addr = "rmi://"+host+":"+port;
        Registry r = registries.get(addr);
        if(r == null){
            RMISocketLocalServer s = new RMISocketLocalServer();
            r = new RegistryReference(addr, new RMISocketClient(), s);
            s.setRegistry((RegistryReference)r);
            registries.put(addr, r);
        }
        return r;
    }
    
    public static void createServer() throws RemoteException{
        createServer(STANDART_PORT);
    }

    public static void createServer(int port) throws RemoteException {
        if(server!= null && server.isRunning()){
            return;
        }
        server = new RMISocketNameServer();
        server.start(port);
    }
    
    public static void stopServer() throws RemoteException{
        if(server!= null && server.isRunning()){
                server.stop();
            
        }
    }
}
