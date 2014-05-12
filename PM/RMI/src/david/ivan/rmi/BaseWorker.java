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
 * Classe base de um processo que fica rodando realizando um trabalho repetidamente
 * Feita para trabalhos que bloqueiam a thread, como espera de conexão, ou leitura de uma stream vinda de um socket
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
    
    public void stop() throws IOException{
        this.running = false;
    }
    
    protected abstract boolean doWork();

    @Override
    public void run() {
        while(this.running){
            if(!this.doWork()){
                try {
                    this.stop();
                } catch (IOException ex) {
                    Logger.getLogger(BaseWorker.class.getName()).log(Level.SEVERE, "LOG", ex);
                }
            }
        }
    }
    
    
    
}
