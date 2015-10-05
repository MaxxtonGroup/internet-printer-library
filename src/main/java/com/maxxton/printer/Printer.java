package com.maxxton.printer;

import com.maxxton.printer.lpr.LPRPrintJob;
import com.maxxton.printer.raw.RawPrintJob;
import java.util.ArrayList;

/**
 * Printer object which contains information about the printer
 *
 * @author Hermans.S Copyright Maxxton 2015
 */
public class Printer
{

  private final String host;
  private int port = -1;
  private int timeout = 5000;

  private ArrayList<PrinterListener> listeners = new ArrayList();

  /**
   * Create new LPRPrinter
   *
   * @param host Printer ip address
   */
  public Printer(String host)
  {
    this.host = host;
  }

  /**
   * Create new LPRPrinter
   *
   * @param host Printer ip address
   * @param port Printer port
   */
  public Printer(String host, int port)
  {
    this.host = host;
    this.port = port;
  }

  /**
   * Schedule PrintJob
   *
   * @param document Document to be printed
   * @param protocol Print protocol
   * @return PrintJob
   */
  public PrintJob print(PrintDocument document, PrintProtocol protocol)
  {
    PrintJob printJob;
    if (protocol.equals(PrintProtocol.LPR))
    {
      printJob = new LPRPrintJob(this, document);
    } else if (protocol.equals(PrintProtocol.RAW))
    {
      printJob = new RawPrintJob(this, document);
    } else
    {
      throw new IllegalArgumentException("Unknown protocol");
    }

    PrintJobScheduler.schedule(printJob);
    return printJob;
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

  public PrinterListener[] getPrintListeners()
  {
    return listeners.toArray(new PrinterListener[listeners.size()]);
  }

}
