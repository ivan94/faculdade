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
public interface ChatClientService extends Remote{
    public void receiveMessage(String from, String message) throws RemoteException;
}
