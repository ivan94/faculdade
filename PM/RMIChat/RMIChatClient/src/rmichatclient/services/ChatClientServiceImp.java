/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmichatclient.services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import rmichatclient.ClienteFrame;

/**
 *
 * @author Ivan
 */
public class ChatClientServiceImp extends UnicastRemoteObject implements ChatClientService{
    
    private final ClienteFrame frame;
    
    public ChatClientServiceImp(ClienteFrame frame) throws RemoteException {
        this.frame = frame;
    }
    
    @Override
    public void receiveMessage(String from, String message) throws RemoteException{
        this.frame.messageReceived(from, message);
    }
    
}
