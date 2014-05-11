/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import java.io.Serializable;

/**
 *  Wrapper de endereço de conexão, usado para manter a interface registry
 *  Apenas encapsula um endereço, mas implementando a interface remote, para poder ser usado no serviço de nomes
 * @author Ivan
 */
public class RemoteAddress implements Remote, Serializable{
    private String address;
    private String name;

    public RemoteAddress(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
