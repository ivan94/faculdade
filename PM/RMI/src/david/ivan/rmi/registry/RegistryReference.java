/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.registry;

import david.ivan.rmi.RMIClient;
import david.ivan.rmi.RMIServer;
import david.ivan.rmi.Remote;
import david.ivan.rmi.RemoteAddress;
import david.ivan.rmi.RemoteInvocationHandler;
import david.ivan.rmi.exceptions.RemoteException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Referencia para o servidor de nomes
 * É o intermediador da aplicação. As chamadas de métodos devem enviar requisições para o servidor de nomes
 * @author ivan
 */
public class RegistryReference implements Registry{
    private RMIClient client;
    private RMIServer server;
    private String address;
    
    private HashMap<String, Remote> boundObjects;

    public RegistryReference(String address, RMIClient client, RMIServer server) {
        this.client = client;
        this.server = server;
        this.address = address;
        this.boundObjects = new HashMap<String, Remote>();
    }
    
    @Override
    public Remote lookup(String name) throws RemoteException {
        String endpoitAddress = client.lookup(address, name);
        Class type = client.getRemoteType(endpoitAddress, name);
        Remote proxy = (Remote)Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, new RemoteInvocationHandler(client, endpoitAddress, name, this));
        return proxy;
    }

    @Override
    public void bind(String name, Remote obj) throws RemoteException {
        if(!this.server.isRunning()){
            this.server.start();
        }
        client.bind(address, name, this.server.getPort());
        this.boundObjects.put(name, obj);
    }

    @Override
    public void unbind(String name) throws RemoteException {
        this.client.unbind(address, name);
        this.boundObjects.remove(name);
        if(this.boundObjects.isEmpty() && this.server.isRunning()){
            this.server.stop();
        }
    }
    
    public String getRemoteType(String name) throws RemoteException{
        Remote obj = this.boundObjects.get(name);
        if(obj == null){
            throw new RemoteException();
        }
        Class remoteInterface = null;
        for(Class itf : obj.getClass().getInterfaces()){
            if(Remote.class.isAssignableFrom(itf)){
                remoteInterface = itf;
                break;
            }
        }
        if(remoteInterface == null) throw new RemoteException();
        return remoteInterface.getName();
    }
    
    public Object invoke(String name, String method, Object[] args) throws RemoteException{
        try {
            Remote obj = this.boundObjects.get(name);
            Object[] arguments = args.clone();
            Class[] argTypes = new Class[args.length];
            for(int i=0; i<args.length; i++){
                if(args[i].getClass().equals(Byte.class)){
                    argTypes[i] = byte.class;
                }else if(args[i].getClass().equals(Short.class)){
                    argTypes[i] = short.class;
                }else if(args[i].getClass().equals(Integer.class)){
                    argTypes[i] = int.class;
                }else if(args[i].getClass().equals(Long.class)){
                    argTypes[i] = long.class;
                }else if(args[i].getClass().equals(Float.class)){
                    argTypes[i] = float.class;
                }else if(args[i].getClass().equals(Double.class)){
                    argTypes[i] = double.class;
                }else if(args[i].getClass().equals(Character.class)){
                    argTypes[i] = char.class;
                }else if(args[i].getClass().equals(Boolean.class)){
                    argTypes[i] = boolean.class;
                }else if(args[i].getClass().equals(RemoteAddress.class)){
                    RemoteAddress ra = (RemoteAddress) args[i];
                    Remote rmt = this.lookup(ra.getName());
                    arguments[i] = rmt;
                    Class[] interfaces = rmt.getClass().getInterfaces();
                    for(Class interf: interfaces){
                        if(Remote.class.isAssignableFrom(interf)){
                            argTypes[i] = interf;
                        }
                    }
                }else{
                    argTypes[i] = args[i].getClass();
                }
                
            }
            return obj.getClass().getMethod(method, argTypes).invoke(obj, arguments);            
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(RegistryReference.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException(ex);
        } catch (SecurityException ex) {
            throw new RemoteException(ex);
        } catch (IllegalAccessException ex) {
            throw new RemoteException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RemoteException(ex);
        } catch (InvocationTargetException ex) {
            throw new RemoteException(ex);
        }
        
    }

    @Override
    protected void finalize() throws Throwable {
        for(String name : this.boundObjects.keySet()){
            this.unbind(name);
        }
    }
    
    
    
    
    
}
