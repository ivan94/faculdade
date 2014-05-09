/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import david.ivan.rmi.exceptions.RemoteException;

/**
 * Interface básica de um servidor
 * O comportamento básico de um servidor é esperar requisições, processar e enviar respostas
 * Interface de comunicação de mais alto nível, usada no resto da biblioteca
 * 
 * Name server é usado no servidor de nomes, e responde requisições como bind unbind e lookup
 * Local server é o servidor local da aplicação que contém objetos remotos registrados, responde requisições como invocação de métodos remotos
 * @author Ivan
 */
public interface RMIServer {
    static final int STANDART_PORT = 5572;
    
    void startNameServer() throws RemoteException;
    void startNameServer(int port) throws RemoteException;
    void stopNameServer() throws RemoteException;
    void startLocalServer() throws RemoteException;
    void startLocalServer(int port) throws RemoteException;
    void stopLocalServer() throws RemoteException;
}
