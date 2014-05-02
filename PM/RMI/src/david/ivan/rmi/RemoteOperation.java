/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

/**
 *
 * @author Ivan
 */
enum RemoteOperation {
    LOOKUP(0), BIND(1), UNBIND(2), INVOKE(3), ERROR(0xff);

    private final int val;
    private RemoteOperation(int val) {
        this.val = val;
    }
    
    public int getVal(){return this.val;}
    
    
}
