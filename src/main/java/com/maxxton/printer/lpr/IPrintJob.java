
package com.maxxton.printer.lpr;

/**
 * Interface for PrintJobs
 * 
 * @author Hermans.S
 * @copyright Maxxton 2015
 */
public interface IPrintJob
{

  /**
   * Execute PrintJob
   * @throws LPRException 
   */
  public void print() throws LPRException;

  /**
   * Get LPRPrinter for this job
   * @return LPRPrinter
   */
  public LPRPrinter getPrinter();

  /**
   * Is the PrintJob running
   * @return true if this job is running, otherwise false
   */
  public boolean isRunning();

}
