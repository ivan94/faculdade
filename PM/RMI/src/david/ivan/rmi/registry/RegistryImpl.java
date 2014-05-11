/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.registry;

import david.ivan.rmi.Remote;
import david.ivan.rmi.exceptions.RemoteException;
import java.util.HashMap;

/**
 * Implementação local do servidor de nomes Associa nomes de interfaces remotas
 * a endereços das interfaces
 *
 * @author Ivan
 */
public class RegistryImpl implements Registry {

    private final HashMap<String, Remote> remotes;

    public RegistryImpl() {
        this.remotes = new HashMap<String, Remote>();
    }

    @Override
    public synchronized Remote lookup(String name) throws RemoteException {
        Remote r = this.remotes.get(name);
        if (r == null) {
            throw new RemoteException();
        } else {
            return r;
        }
    }

    @Override
    public synchronized void bind(String name, Remote obj) throws RemoteException {
        if (!this.remotes.containsKey(name)) {
            this.remotes.put(name, obj);
        } else {
            throw new RemoteException();
        }
    }

    @Override
    public synchronized void unbind(String name) throws RemoteException {
        Remote r = this.remotes.remove(name);
        if (r == null) {
            throw new RemoteException();
        }
    }

}
