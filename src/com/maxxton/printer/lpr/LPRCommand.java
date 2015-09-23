
package com.maxxton.printer.lpr;

/**
 * LPR commands
 * @author hermans.s
 */
public enum LPRCommand {
    
    NULL(0),
    SOH(1),
    STX(2),
    ETX(3),
    /** Horizontal tab */
    HT(9),
    /** Line feed */
    LF(10),
    /** Paper cut */
    GS_V(29, 86),
    
    /** Set Character set */
    ESC_37(27,37),
    /** Space */
    SPACE(127);
    
    private byte[] ascii;
    
    private LPRCommand(int... ascii){
        byte[] bytes = new byte[ascii.length];
        for(int i = 0; i < ascii.length; i++){
            bytes[i] = (byte) ascii[i];
        }
        this.ascii = bytes;
    }
    
    private LPRCommand(byte... ascii){
        this.ascii = ascii;
    }
    
    protected byte[] getCode(){
        return ascii;
    }
    
}
