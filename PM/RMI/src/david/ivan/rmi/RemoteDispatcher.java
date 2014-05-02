/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import david.ivan.rmi.exceptions.RemoteException;


/**
 *
 * @author Ivan
 */
interface RemoteDispatcher {
    void connect(String url) throws IllegalArgumentException, RemoteException;
    void dispatch(RemoteOperation operation, Object[] data) throws IllegalArgumentException, RemoteException;
}
