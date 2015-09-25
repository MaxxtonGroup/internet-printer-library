
package com.maxxton.printer.lpr;

/**
 * This Exception is throwen during printing
 * 
 * @author Hermans.S
 * Copyright Maxxton 2015
 * 
 * @see PrintJob#print()
 */
public class LPRException extends Exception
{

  public LPRException(String msg)
  {
    super(msg);
  }

  public LPRException(Throwable e)
  {
    super(e);
  }

  public LPRException(String msg, Throwable e)
  {
    super(msg, e);
  }
}
