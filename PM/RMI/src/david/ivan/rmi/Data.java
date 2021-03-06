/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

/**
 * Interface base que encapsula dados enviados pela rede
 * Usado em processor, para se implementar protocolos de comunicação
 * @author Ivan
 */
public interface Data {
    String getAddress();
    void setAddress(String address);
    Object getData();
    RemoteOperation getRemoteOperation();
}
