/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public abstract class BaseListener implements Runnable{
    private Thread t;
    private boolean running;

    public BaseListener() {
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }
    
    public void start() throws IOException{
        t = new Thread(this);
        this.running = true;
        this.t.start();
    }
    
    public void stop(){
        this.running = false;
    }
    
    protected abstract void listen() throws IOException;

    @Override
    public void run() {
        while(this.running){
            try{
                this.listen();
            }catch(IOException ex){
                this.running = false;
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, null, ex);
            }
        }
    }
    
    
    
}
