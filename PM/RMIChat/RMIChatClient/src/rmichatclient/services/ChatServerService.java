/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmichatclient.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Ivan
 */
public interface ChatServerService extends Remote{

    /**
     *
     * @param client
     * @param username
     * @throws RemoteException
     */
    public void connectToServer(ChatClientService client, String username) throws RemoteException;
    public void sendMessage(String message) throws RemoteException;
    
    
}
