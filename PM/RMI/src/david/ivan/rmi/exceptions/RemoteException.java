/*
 * Classe base de exceções da biblioteca
 */

package david.ivan.rmi.exceptions;

/**
 *
 * @author Ivan
 */
public class RemoteException extends Exception{

    public RemoteException() {
    }
    
    public RemoteException(String msg){
        super(msg);
    }
    
    public RemoteException(Throwable t){
        super(t);
    }
    
}
