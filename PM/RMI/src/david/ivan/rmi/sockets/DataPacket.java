/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

/**
 *
 * @author ivan
 */
public class DataPacket {
    private int operation;
    private byte[] data;
    private int checksum;

    public DataPacket(byte operation, byte[] data, int checksum) {
        this.operation = ((int)operation) & 0xff; //garante que é tratado como positivo após converter
        this.data = data;
        this.checksum = checksum;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(byte operation) {
        this.operation = operation;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getChecksum() {
        return checksum;
    }

    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }
    
    public void generateChecksum(){
        this.checksum = this.calculateChecksum();
    }
    
    private int calculateChecksum(){
        int chk = 0;
        chk += this.operation;
        chk += this.data.length;
        for(byte b: this.data){
            chk += b;
        }
        return chk;
    }
    
    public boolean isCheckSumValid(){
        return this.checksum == this.calculateChecksum();
    }
    
    public SocketOperation getSocketOperation(){
        switch(this.operation){
            case 0:
                return SocketOperation.LOOKUP;
            case 1:
                return SocketOperation.BIND;
            case 2:
                return SocketOperation.UNBIND;
            case 3:
                return SocketOperation.INVOKE;
            case 0xff:
                return SocketOperation.ERROR;
            default:
                return SocketOperation.UNKNOWN;
        }
    }
}
