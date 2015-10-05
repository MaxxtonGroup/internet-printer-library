
package com.maxxton.printer;

/**
 *
 * Adapter class for the PrinterListener
 * 
 * @author Hermans.S
 * Copyright Maxxton 2015
 */
public abstract class PrintAdapter implements PrinterListener
{

  @Override
  public void printSucceed(PrintEvent event){}

  @Override
  public void printFailed(PrintEvent event, PrintException e){}

}
