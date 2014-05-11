/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

/**
 *  Wrapper de endereço de conexão, usado para manter a interface registry
 *  Apenas encapsula um endereço, mas implementando a interface remote, para poder ser usado no serviço de nomes
 * @author Ivan
 */
public class RemoteAddress implements Remote{
    private String address;

    public RemoteAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
