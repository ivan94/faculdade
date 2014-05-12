/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import david.ivan.rmi.Processor;
import david.ivan.rmi.registry.Registry;
import david.ivan.rmi.registry.RegistryReference;

/**
 *
 * @author Ivan
 */
public class RMISocketLocalServer extends RMISocketServer{
    private RegistryReference registry;
    private LocalSocketProcessor processor;
    
    
    @Override
    public Registry getRegistry() {
        if(this.registry == null)
            throw new NullPointerException();
        return this.registry;
    }

    public void setRegistry(RegistryReference registry) {
        this.registry = registry;
    }
    

    @Override
    public Processor getProcessor() {
        if(this.processor == null)
            this.processor = new LocalSocketProcessor(this);
        return this.processor;
    }
}
