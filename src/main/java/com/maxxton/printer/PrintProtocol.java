
package com.maxxton.printer;

/**
 * Supported Print protocols
 * @author Hermans.S
 */
public enum PrintProtocol {
    
    RAW(9100), LPR(515);
    
    private final int port;
    
    private PrintProtocol(int port){
        this.port = port;
    }
    
    public int getPort(){
        return port;
    }
    
}
