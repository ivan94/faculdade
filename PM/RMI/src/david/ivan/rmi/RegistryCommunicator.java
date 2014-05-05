/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import java.net.URL;

/**
 *
 * @author ivan
 */
public interface RegistryCommunicator {

    public URL findRemote(String name);

    public void bind(String name);

    public void unbind(String name);
    
}
