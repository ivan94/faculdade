/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.sockets;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author ivan
 */
public abstract class PacketProcessor implements Runnable {

    private static PacketProcessor processor;

    public synchronized static PacketProcessor getProcessor() {
        return processor;
    }

    public synchronized static void setProcessor(PacketProcessor processor) {
        PacketProcessor.processor = processor;
    }

    private final ConcurrentLinkedQueue<DataPacket> packets;
    private Thread t;
    private boolean running;

    public PacketProcessor() {
        this.packets = new ConcurrentLinkedQueue<DataPacket>();
        this.running = false;
    }

    public void start() {
        this.t = new Thread(this);
        this.running = true;
        this.t.start();
    }

    public void stop() {
        this.running = false;
    }

    public abstract void process(DataPacket packet);

    public synchronized void register(DataPacket packet) {
        this.packets.add(packet);
        this.notify();
    }

    @Override
    public void run() {
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
