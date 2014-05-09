/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi.sockets;

import david.ivan.rmi.Data;
import david.ivan.rmi.RemoteOperation;

/**
 *
 * @author ivan
 */
public class DataPacket implements Data{
    private int operation;
    private byte[] data;
    private int checksum;
    private String address;

    public DataPacket(String address, byte operation, byte[] data, int checksum) {
        this.operation = ((int)operation) & 0xff; //garante que é tratado como positivo após converter
        this.data = data;
        this.checksum = checksum;
        this.address = address;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(byte operation) {
        this.operation = operation;
    }

    @Override
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
    
    @Override
    public RemoteOperation getRemoteOperation(){
        switch(this.operation){
            case 0:
                return RemoteOperation.LOOKUP;
            case 1:
                return RemoteOperation.BIND;
            case 2:
                return RemoteOperation.UNBIND;
            case 3:
                return RemoteOperation.INVOKE;
            case 0xff:
                return RemoteOperation.ERROR;
            default:
                return RemoteOperation.UNKNOWN;
        }
    }

    @Override
    public String getAddress() {
       return this.address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }
}
