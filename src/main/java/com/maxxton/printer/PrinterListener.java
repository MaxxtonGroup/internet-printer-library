package com.maxxton.printer;

/**
 * Listen for PrintEvent. This Listener should be attached to a LPRPrinter
 *
 * @author Hermans.S Copyright Maxxton 2015
 * @see PrintEvent
 * @see PrintJob
 */
public interface PrinterListener
{

  public void printSucceed(PrintEvent event);

  public void printFailed(PrintEvent event, PrintException e);

}
