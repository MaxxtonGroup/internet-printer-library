
package com.maxxton.printer.lpr;

/**
 *
 * @author Hermans.S
 */
public abstract class TestJob implements IPrintJob{
    
    private final LPRPrinter printer;
    
    public TestJob(LPRPrinter printer){
        this.printer = printer;
    }

    public boolean isRunning()
    {
      return true;
    }

    public LPRPrinter getPrinter()
    {
      return printer;
    }
    
}
