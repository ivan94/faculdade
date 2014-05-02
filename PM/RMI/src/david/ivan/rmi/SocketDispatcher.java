/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import david.ivan.rmi.exceptions.RemoteException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
class SocketDispatcher implements RemoteDispatcher, Runnable{
    static final int ERRORLIMIT = 10;
    
    private static SocketDispatcher singleton = null;
    
    private Socket socket = null;
    private Thread thread = null;
    private ConcurrentLinkedQueue<Byte[]> queue = null;
    
    private SocketDispatcher(){
        this.queue = new ConcurrentLinkedQueue<Byte[]>();
    }
    
    public static SocketDispatcher getInstance(){
        //Cria um dispatcher singleton com uma thread rodando
        if(singleton == null){
            singleton = new SocketDispatcher();
        }
        
        if(singleton.thread == null){
            singleton.thread = new Thread(singleton);
        }
        
        if(!singleton.thread.isAlive()){
            singleton.thread.start();
        }
        return singleton;
    }
    
    @Override
    public void connect(String url) throws IllegalArgumentException, RemoteException {
        try {
            String[] urlParts = url.split(":");
            if(this.socket == null){
                if(urlParts.length == 1){
                    this.socket = new Socket(urlParts[0], 666); //PORTA PADRÃO TODO: SETAR PRA UMA CONSTANTE
                }else if(urlParts.length == 2){
                    this.socket = new Socket(urlParts[0], Integer.parseInt(urlParts[1]));
                }else{
                    throw new IllegalArgumentException("URL não atende ao padrão");
                }  
            }
            this.thread.notify();
        } catch (IOException ex) {
            Logger.getLogger(SocketDispatcher.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException("URL não atende ao padrão");
        }
    }

    @Override
    public void dispatch(RemoteOperation operation, Object[] data) throws IllegalArgumentException, RemoteException {
        switch(operation){
            case BIND:
                this.queue.add(this.getBindPacket(data));
                break;
        }//FALTA CONTINUAR COM O SWITCH
    }
    
    
    private Byte[] getPacket(byte op, byte[] data){
        int size = data.length;
        Byte[] pack = new Byte[1+4+size+4];
        pack[0] = op;
        pack[1] = (byte)((size >> 24) & 0xff);
        pack[2] = (byte)((size >> 16) & 0xff);
        pack[3] = (byte)((size >> 8) & 0xff);
        pack[4] = (byte)((size) & 0xff);
        int offset = 1+4;
        for(byte d : data){
            pack[offset++] = d;
        }
        int checksum = 0;
        for(int i = 0; i < checksum; i++){
            checksum += pack[i];
        }
        pack[offset + 0] = (byte)((checksum >> 24) & 0xff);
        pack[offset + 1] = (byte)((checksum >> 16) & 0xff);
        pack[offset + 2] = (byte)((checksum >> 8) & 0xff);
        pack[offset + 3] = (byte)((checksum) & 0xff);
        return pack;
    }
    
    private Byte[] getBindPacket(Object[] data){
        try {
            byte op = 1;
            String connectionString = (String) data[0];
            byte[] dataBytes = connectionString.getBytes("US-ASCII");//TODO: colocar charset como constante

            return this.getPacket(op, dataBytes);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalArgumentException(ex);
        } catch (ClassCastException ex){
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public void run() {
        int errorcount = 0;
        while(true){
            try{
                if(this.socket != null && this.socket.isConnected() && this.queue.size() > 0){
                    OutputStream os =  this.socket.getOutputStream();
                    Byte[] boxedPacket = this.queue.remove();
                    byte[] packet = new byte[boxedPacket.length];
                    int i = 0;
                    for(Byte b : boxedPacket){
                        packet[i++] = b;
                    }
                    os.write(packet);
                }else{
                    this.thread.wait();
                }
            }catch(IOException e){
                if(errorcount <= ERRORLIMIT ){
                    Logger.getLogger(SocketDispatcher.class.getName()).log(Level.SEVERE, null, e);
                    errorcount++;
                }else{
                    this.thread.interrupt();
                }
            } catch (InterruptedException ex) { //Nunca deveria ser chamado, indica erro grave de implementação
                Logger.getLogger(SocketDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                this.thread.interrupt();
            }
        }
    }
    
}
