
package com.maxxton.printer.lpr;

/**
 *
 * @author hermans.s
 */
public interface IPrintJob {
    
    public void print() throws LPRException;
    
    public LPRPrinter getPrinter();
    
    public boolean isRunning();
    
}
