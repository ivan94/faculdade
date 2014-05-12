/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import david.ivan.rmi.exceptions.RemoteException;
import david.ivan.rmi.registry.Registry;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @author Ivan
 */
public class RemoteInvocationHandler implements InvocationHandler{
    private final RMIClient client;
    private final String address;
    private final String name;
    private final Registry registry;

    public RemoteInvocationHandler(RMIClient client, String address, String name, Registry registry) {
        this.client = client;
        this.address = address;
        this.name = name;
        this.registry = registry;
    }
    
    
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object[] arguments = new Object[args.length];
        for(int i=0; i<args.length; i++){
            if(Remote.class.isAssignableFrom(args[i].getClass())){
                
                Class[] interfaces = args[i].getClass().getInterfaces();
                RemoteAddress ra = new RemoteAddress(null, null);
                for(Class intf: interfaces){
                    if(Remote.class.isAssignableFrom(intf)){
                        try{registry.bind(intf.getName(), (Remote)args[i]);}catch(RemoteException ex){} //Não há problema se a classe já 'is bounded'
                        arguments[i] = new RemoteAddress(null, intf.getName());
                        break;
                    }
                }
            }else{
                arguments[i] = args[i];
            }
        }
        return client.invoke(address, name, method.getName(), arguments);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
        this.client.closeConnection(address);
    }
    
    
    
}
