/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import david.ivan.rmi.exceptions.RemoteException;

/**
 *
 * @author Ivan
 */
public interface RMIServer {
    static final int STANDART_PORT = 5572;
    
    void startNameServer() throws RemoteException;
    void startNameServer(int port) throws RemoteException;
    void stopNameServer() throws RemoteException;
    void startLocalServer() throws RemoteException;
    void startLocalServer(int port) throws RemoteException;
    void stopLocalServer() throws RemoteException;
}
