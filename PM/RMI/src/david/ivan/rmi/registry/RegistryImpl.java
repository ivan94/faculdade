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
public class RegistryImpl implements Registry{
    

    @Override
    public synchronized Remote lookup(String name) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void bind(String name, Remote obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void unbind(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
