
package com.maxxton.printer.lpr;

/**
 *
 * Adapter class for the PrinterListener
 * 
 * @author Hermans.S
 * @copyright Maxxton 2015
 */
public abstract class PrintAdapter implements PrinterListener
{

  public void printSucceed(PrintEvent event){}

  public void printFailed(PrintEvent event, LPRException e){}

}
