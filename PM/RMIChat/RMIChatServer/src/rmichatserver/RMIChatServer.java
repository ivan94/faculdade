/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmichatserver;

import david.ivan.rmi.Naming;
import david.ivan.rmi.exceptions.RemoteException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmichatserver.services.ChatServerServiceImp;
import rmichatservices.ChatServerService;



/**
 *
 * @author Ivan
 */
public class RMIChatServer {
    
    
    public static void main(String[] args)throws RemoteException, IOException{
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        ChatServerServiceImp css = new ChatServerServiceImp();
        try {
            Naming.bind(ChatServerService.class.getSimpleName(), css);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        String command = "";
        while(!command.contains("quit")){
            command = rd.readLine();
        }
        Naming.unbind(ChatServerService.class.getSimpleName());
    }
    
    
    
}
