package com.maxxton.printer;

/**
 * This Exception is throwen during printing
 *
 * @author Hermans.S Copyright Maxxton 2015
 *
 * @see PrintJob#print()
 */
public class PrintException extends Exception
{
  
  public PrintException(){
    super();
  }

  public PrintException(String msg)
  {
    super(msg);
  }

  public PrintException(Throwable e)
  {
    super(e);
  }

  public PrintException(String msg, Throwable e)
  {
    super(msg, e);
  }
}
