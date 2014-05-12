/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package registry;

import david.ivan.rmi.exceptions.RemoteException;
import david.ivan.rmi.registry.LocateRegistry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Ivan
 */
public class Registry {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        LocateRegistry.createServer();
        String command = "";
        while(!command.contains("quit")){
            command = rd.readLine();
        }
        LocateRegistry.stopServer();
    }
    
}
