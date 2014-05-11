/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.registry;

import david.ivan.rmi.NodeCommunicator;
import david.ivan.rmi.Remote;
import david.ivan.rmi.RegistryCommunicator;
import david.ivan.rmi.RemoteAddress;
import david.ivan.rmi.exceptions.RemoteException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Referencia para o servidor de nomes
 * É o intermediador da aplicação. As chamadas de métodos devem enviar requisições para o servidor de nomes
 * @author ivan
 */
public class RegistryReference implements Registry{
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
            Logger.getLogger(RegistryReference.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassCastException ex){
            Logger.getLogger(RegistryReference.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
    
}
