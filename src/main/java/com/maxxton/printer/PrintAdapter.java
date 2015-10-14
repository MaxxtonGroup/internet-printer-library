package com.maxxton.printer;

/**
 *
 * Adapter class for the PrinterListener
 *
 * Copyright Maxxton 2015
 *
 * @author Hermans.S
 */
public abstract class PrintAdapter implements PrinterListener
{

  @Override
  public void printSucceed(PrintEvent event)
  {
  }

  @Override
  public void printFailed(PrintEvent event, PrintException e)
  {
  }

}
