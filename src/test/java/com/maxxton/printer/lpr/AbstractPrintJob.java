
package com.maxxton.printer.lpr;

/**
 * Abstract PrintJob for testing the PrintJobScheduler 
 *
 * @see JobSchedulerTest
 *
 * @author Hermans.S
 * @copyright Maxxton 2015
 */
public abstract class AbstractPrintJob implements IPrintJob{
    
    private final LPRPrinter printer;
    
    public AbstractPrintJob(LPRPrinter printer){
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
