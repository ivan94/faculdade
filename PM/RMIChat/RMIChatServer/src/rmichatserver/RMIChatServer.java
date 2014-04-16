/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmichatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import rmichatserver.services.ChatServerServiceImp;
import rmichatservices.ChatServerService;

/**
 *
 * @author Ivan
 */
public class RMIChatServer {

    /**
     * @param args the command line arguments
     * @throws java.rmi.AlreadyBoundException
     * @throws java.net.MalformedURLException
     * @throws java.rmi.RemoteException
     */
    
    static Registry registry;
    
    public static void main(String[] args) throws AlreadyBoundException, MalformedURLException, RemoteException, IOException, NotBoundException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        registry = LocateRegistry.createRegistry(6666);
        ChatServerServiceImp css = new ChatServerServiceImp();
        registry.bind(ChatServerService.class.getSimpleName(), css);
        String command = "";
        while(!command.contains("quit")){
            command = rd.readLine();
        }
        registry.unbind(ChatServerService.class.getSimpleName());
        ChatServerServiceImp.unexportObject(css, false);
        
        
        
        
        
    }
    
    
    
}
