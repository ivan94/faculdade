/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.registry;

import david.ivan.rmi.Remote;
import david.ivan.rmi.exceptions.RemoteException;

/**
 *
 * @author Ivan
 */
public interface Registry {
    public Remote lookup(String name) throws RemoteException;
    public void bind(String name, Remote obj) throws RemoteException;
    public void unbind(String name) throws RemoteException;
}
