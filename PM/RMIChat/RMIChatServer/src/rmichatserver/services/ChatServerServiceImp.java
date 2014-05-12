/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmichatserver.services;

import david.ivan.rmi.exceptions.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import rmichatservices.ChatClientService;
import rmichatservices.ChatServerService;

/**
 *
 * @author Ivan
 */
public class ChatServerServiceImp implements ChatServerService{
    
    private final HashMap<Integer, String> clientNames;
    private final HashMap<Integer, ChatClientService> callbacks;
    private int nextId = 1;
    

    public ChatServerServiceImp() throws RemoteException {
        this.clientNames = new HashMap<Integer, String>();
        this.callbacks = new HashMap<Integer, ChatClientService>();
    }   

    @Override
    public int connectToServer(ChatClientService client, String username) throws RemoteException {
        int id = this.nextId;
        this.nextId++;
        clientNames.put(id, username);
        callbacks.put(id, client);
        return id;
    }
    
    
    

    @Override
    public void sendMessage(int id, String message) throws RemoteException {
        Iterator<Map.Entry<Integer, ChatClientService>> i =  this.callbacks.entrySet().iterator();
        while(i.hasNext()){
            Map.Entry<Integer, ChatClientService> entry = i.next();
            entry.getValue().receiveMessage(this.clientNames.get(entry.getKey()), message);
        }   
        
    }
    
}
