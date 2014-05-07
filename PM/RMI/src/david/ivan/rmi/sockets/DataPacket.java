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
class DataPacket {
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
    
    public boolean isCheckSumValid(){
        int chk = this.checksum;
        chk -= this.operation;
        chk -= this.data.length;
        for(byte b: this.data){
            chk -= b;
        }
        return chk == 0;
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

enum SocketOperation {
    LOOKUP(0), BIND(1), UNBIND(2), INVOKE(3), ERROR(0xff), UNKNOWN(0xfe);

    private final int val;
    private SocketOperation(int val) {
        this.val = val;
    }
    
    public int getVal(){return this.val;}  
}
