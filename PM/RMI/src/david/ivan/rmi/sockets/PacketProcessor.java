/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package david.ivan.rmi.sockets;

import david.ivan.rmi.Processor;

/**
 *
 * @author ivan
 */
public abstract class PacketProcessor extends Processor implements Runnable {
    public synchronized void register(DataPacket data) {
        super.register(data);
    }
    public abstract void process(DataPacket packet);
}
