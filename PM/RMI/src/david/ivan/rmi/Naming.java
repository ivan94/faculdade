/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import david.ivan.rmi.exceptions.RemoteException;
import david.ivan.rmi.registry.LocateRegistry;

/**
 *
 * @author Ivan
 */
public class Naming {
    public static void bind(String name, Remote remote) throws RemoteException{
        bind(name, "localhost", remote);
    }
    public static void bind(String name, String host, Remote remote) throws RemoteException{
        LocateRegistry.getRegistry(host).bind(name, remote);
    }
    public static void bind(String name, String host, int port, Remote remote) throws RemoteException{
        LocateRegistry.getRegistry(host, port).bind(name, remote);
    }
    
    public static void unbind(String name) throws RemoteException{
        unbind(name, "localhost");
    }
    public static void unbind(String name, String host) throws RemoteException{
        LocateRegistry.getRegistry(host).unbind(name);
    }
    public static void unbind(String name, String host, int port) throws RemoteException{
        LocateRegistry.getRegistry(host, port).unbind(name);
    }
    
    public static Remote lookup(String name) throws RemoteException{
        return lookup("localhost", name);
    }

    public static Remote lookup(String host, String name) throws RemoteException {
        return LocateRegistry.getRegistry(host).lookup(name);
    }
    public static Remote lookup(String host, int port, String name) throws RemoteException {
        return LocateRegistry.getRegistry(host, port).lookup(name);
    }
}
