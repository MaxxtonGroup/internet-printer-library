
package com.maxxton.printer.lpr;

/**
 *
 * @author hermans.s
 */
public class LPRException extends Exception {

    public LPRException(String msg) {
        super(msg);
    }
    
    public LPRException(Throwable e){
        super(e);
    }
    
    public LPRException(String msg, Throwable e){
        super(msg, e);
    }
}
