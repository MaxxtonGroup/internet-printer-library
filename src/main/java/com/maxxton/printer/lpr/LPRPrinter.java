
package com.maxxton.printer.lpr;

import java.util.ArrayList;

/**
 * Printer object which contains information about the printer
 * 
 * @author Hermans.S
 * Copyright Maxxton 2015
 */
public class LPRPrinter
{

  private static final int DEFAULT_LPR_PORT = 515;

  private final String host;
  private final int port;
  private int timeout = 5000;

  private ArrayList<PrinterListener> listeners = new ArrayList();

  /**
   * Create new LPRPrinter
   * 
   * @param host
   *          Printer ip address
   */
  public LPRPrinter(String host)
  {
    this.host = host;
    this.port = DEFAULT_LPR_PORT;
  }

  /**
   * Create new LPRPrinter
   * 
   * @param host
   *          Printer ip address
   * @param port
   *          Printer LPR port (default = 515)
   */
  public LPRPrinter(String host, int port)
  {
    this.host = host;
    this.port = port;
  }

  /**
   * Schedule PrintJob
   * 
   * @param document
   *          Document to be printed
   */
  public void print(LPRDocument document)
  {
    PrintJob printJob = new PrintJob(this, document);
    PrintJobScheduler.schedule(printJob);
  }

  public String getHost()
  {
    return host;
  }

  public int getPort()
  {
    return port;
  }

  public int getTimeout()
  {
    return timeout;
  }

  public void setTimeout(int timeout)
  {
    this.timeout = timeout;
  }

  public void addPrintListener(PrinterListener listener)
  {
    listeners.add(listener);
  }

  public void removePrintListener(PrinterListener listener)
  {
    listeners.remove(listener);
  }

  protected PrinterListener[] getPrintListeners()
  {
    return listeners.toArray(new PrinterListener[listeners.size()]);
  }

}
