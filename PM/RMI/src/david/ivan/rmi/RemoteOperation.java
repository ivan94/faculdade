/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

/**
 * Enumeração de operações suportadas pelo servidor
 * Toda operação deve ter uma enumeração a representando
 * Val indica o código da operação
 * @author ivan
 */
public enum RemoteOperation {
    LOOKUP(0), BIND(1), UNBIND(2), INVOKE(3), GETTYPE(4), ERROR(0xff), UNKNOWN(0xfe);

    private final int val;
    private RemoteOperation(int val) {
        this.val = val;
    }
    
    public int getVal(){return this.val;}
}
