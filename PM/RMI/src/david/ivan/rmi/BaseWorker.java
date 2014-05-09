/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public abstract class BaseWorker implements Runnable{
    private Thread t;
    private boolean running;

    public BaseWorker() {
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
    
    protected abstract boolean doWork();

    @Override
    public void run() {
        while(this.running){
            if(!this.doWork()){
                this.stop();
            }
        }
    }
    
    
    
}
