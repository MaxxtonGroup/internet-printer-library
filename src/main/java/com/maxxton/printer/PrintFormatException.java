package com.maxxton.printer;

/**
 * Exception when print format is incorrect
 *
 * Copyright Maxxton 2015
 *
 * @author hermans.s
 */
public class PrintFormatException extends PrintException
{

  public PrintFormatException()
  {
    super();
  }

  public PrintFormatException(String msg)
  {
    super(msg);
  }

  public PrintFormatException(Throwable e)
  {
    super(e);
  }

  public PrintFormatException(String msg, Throwable e)
  {
    super(msg, e);
  }
}
