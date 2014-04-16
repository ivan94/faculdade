/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmichatserver.services;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import rmichatservices.ChatClientService;
import rmichatservices.ChatServerService;

/**
 *
 * @author Ivan
 */
public class ChatServerServiceImp extends UnicastRemoteObject implements ChatServerService{
    
    private final HashMap<String, String> clientNames;
    private final HashMap<String, ChatClientService> callbacks;
    

    public ChatServerServiceImp() throws RemoteException {
        this.clientNames = new HashMap<String, String>();
        this.callbacks = new HashMap<String, ChatClientService>();
    }   
    

    @Override
    public void connectToServer(ChatClientService client, String username) throws RemoteException {
        try{
            this.clientNames.put(RemoteServer.getClientHost(), username);
            this.callbacks.put(RemoteServer.getClientHost(), client);
        }catch(ServerNotActiveException e){
            throw new RuntimeException("Internal call of service not supported");
        }
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        Iterator<Map.Entry<String, ChatClientService>> i =  this.callbacks.entrySet().iterator();
        while(i.hasNext()){
            Map.Entry<String, ChatClientService> entry = i.next();
            entry.getValue().receiveMessage(this.clientNames.get(entry.getKey()), message);
        }
        
        
    }
    
}
