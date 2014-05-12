/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmichatservices;

import david.ivan.rmi.Remote;
import david.ivan.rmi.exceptions.RemoteException;

/**
 *
 * @author Ivan
 */
public interface ChatServerService extends Remote{


    public int connectToServer(ChatClientService client, String username) throws RemoteException;
    public void sendMessage(int id, String message) throws RemoteException;
    
    
}
