/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.registry;

import david.ivan.rmi.NodeCommunicator;
import david.ivan.rmi.Remote;
import david.ivan.rmi.RegistryCommunicator;
import david.ivan.rmi.exceptions.RemoteException;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public class RemoteRegistry implements Registry{
    private RegistryCommunicator rc;
    private NodeCommunicator nc;
    
    private HashMap<String, Remote> boundObjects;
    
    
    @Override
    public Remote lookup(String name) throws RemoteException {
        try {
            URL remoteURL =  rc.findRemote(name);
            String key; //remote object key, must be obtained throught processing of name variable
            nc.connect(remoteURL);
            String typeName = nc.getTypeName();
            Class stub = Class.forName(typeName);
            return (Remote) Proxy.newProxyInstance(stub.getClassLoader(), new Class[] {stub}, null);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteRegistry.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassCastException ex){
            Logger.getLogger(RemoteRegistry.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void bind(String name, Remote obj) {
        rc.bind(name);
        this.boundObjects.put(name, obj);
    }

    @Override
    public void unbind(String name) {
        rc.unbind(name);
        this.boundObjects.remove(name);
    }
    
    
    
}
