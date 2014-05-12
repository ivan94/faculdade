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
public interface RMIClient {
    public void bind(String address, String name, int port) throws RemoteException;
    public void unbind(String address, String name) throws RemoteException;
    public String lookup(String address, String name) throws RemoteException;
    public Object invoke(String address, String name, String method, Object[] args) throws RemoteException;
    public Class getRemoteType(String address, String name) throws RemoteException;
    public void closeConnection(String address) throws RemoteException;
}
