package com.maxxton.printer;

import static com.maxxton.printer.PrintJob.LOG;
import com.maxxton.printer.lpr.LPRPrintJob;
import com.maxxton.printer.lpr.SimpleLPRPrintJob;
import com.maxxton.printer.raw.RawPrintJob;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Printer object which contains information about the printer
 *
 * @author Hermans.S Copyright Maxxton 2015
 */
public class Printer
{

  private String host;
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
    if (protocol.equals(PrintProtocol.RAW))
    {
      printJob = new RawPrintJob(this, document);
    } else if (protocol.equals(PrintProtocol.LPR))
    {
      printJob = new LPRPrintJob(this, document);
    } else if (protocol.equals(PrintProtocol.SIMPLE_LPR))
    {
      printJob = new SimpleLPRPrintJob(this, document);
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

  public void setHost(String host)
  {
    this.host = host;
  }

  public int getPort()
  {
    return port;
  }

  public void setPort(int port)
  {
    this.port = port;
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

  /**
   * Connect to printer
   *
   * @return printer connection
   * @throws PrintException connection error
   */
  public PrinterConnection connect() throws PrintException
  {
    return connect(port);
  }

  /**
   * Connect to printer
   *
   * @param protocol the protocol for this connection
   * @return printer connection
   * @throws PrintException connection error
   */
  public PrinterConnection connect(PrintProtocol protocol) throws PrintException
  {
    return connect(protocol.getPort());
  }

  /**
   * Connect to printer
   *
   * @param port to connect to the printer
   * @return printer connection
   * @throws PrintException connection error
   */
  public PrinterConnection connect(int port) throws PrintException
  {
    try
    {
      LOG.info("Connecting... to " + getHost() + ":" + port);
      Socket socket = new Socket();
      socket.setSoTimeout(getTimeout());
      socket.connect(new InetSocketAddress(getHost(), port), getTimeout());
      LOG.info("Connected");
      return new PrinterConnection(socket);
    } catch (IOException e)
    {
      throw new PrintException("Printer offline", e);
    }
  }

  /**
   * Check if it possible to connect to the printer
   *
   * @return true if the printer is available, otherwise false
   */
  public boolean available()
  {
    return available(port);
  }

  /**
   * Check if it possible to connect to the printer
   *
   * @param protocol protocol for the ocnnection with the printer
   * @return true if the printer is available, otherwise false
   */
  public boolean available(PrintProtocol protocol)
  {
    return available(protocol.getPort());
  }

  /**
   * Check if it possible to connect to the printer
   *
   * @param port number for the connection with the printer
   * @return true if the printer is available, otherwise false
   */
  public boolean available(int port)
  {
    try
    {
      PrinterConnection connection = connect(port);
      try
      {
        connection.close();
      } catch (IOException e)
      {
      }
      return true;
    } catch (PrintException e)
    {
      LOG.warning(e.getMessage());
      return false;
    }
  }

}
