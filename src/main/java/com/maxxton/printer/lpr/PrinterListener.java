
package com.maxxton.printer.lpr;

/**
 *
 * @author hermans.s
 */
public interface PrinterListener
{

  public void printSucceed(PrintEvent event);

  public void printFailed(PrintEvent event, LPRException e);

}
