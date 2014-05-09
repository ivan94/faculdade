/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;


import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Classe base para processar requisições
 * Usada para implementar um servidor que aguarda requisições
 * @author Ivan
 */
public abstract class Processor implements Runnable{
    protected final ConcurrentLinkedQueue<Data> packets;
    private Thread t;
    private boolean running;

    public Processor() {
        this.packets = new ConcurrentLinkedQueue<Data>();
        this.running = false;
    }

    public void start() {
        this.t = new Thread(this);
        this.running = true;
        this.t.start();
    }

    public boolean isRunning() {
        return running;
    }

    public synchronized void stop() {
        this.running = false;
        this.notify();
    }

    public abstract void process(Data data);

    public synchronized void register(Data data) {
        this.packets.add(data);
        this.notify();
    }

    @Override
    public synchronized void run() {
        while (this.running) {
            if (!this.packets.isEmpty()) {
                this.process(this.packets.remove());
            } else {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    this.stop();
                }
            }
        }
    }
}
