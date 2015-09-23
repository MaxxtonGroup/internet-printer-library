
package com.maxxton.printer.lpr;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hermans.s
 */
public class PrintJob implements IPrintJob
{

  public static final Logger LOG = Logger.getLogger(PrintJob.class.getName());

  private final LPRDocument document;
  private final LPRPrinter printer;
  private final String jobId;

  private boolean running = false;
  private String user = System.getProperty("user.name");
  private String hostname;

  /**
   * Create new PrintJob
   * 
   * @param printer
   *          LPR Printer
   * @param document
   *          Document to be printed
   */
  protected PrintJob(LPRPrinter printer, LPRDocument document)
  {
    this.printer = printer;
    this.document = document;
    jobId = getNewJobId();
    try
    {
      hostname = InetAddress.getLocalHost().getHostName();
    }
    catch (Exception e)
    {
    }
  }

  /**
   * Print the document
   * 
   * @throws LPRException
   */
  public void print() throws LPRException
  {
    LOG.info("Start PrintJob '" + document.getDocumentName() + "'");
    running = true;

    try
    {
      // Connect to printer
      Socket printerConnection = connect();
      BufferedReader printerIn = new BufferedReader(new InputStreamReader(printerConnection.getInputStream()));
      DataOutputStream printerOut = new DataOutputStream(printerConnection.getOutputStream());

      // Create control file
      String controlFile = createControlFile();

      // Create document
      byte[] rawDocument = document.getRaw();

      // start sending
      printerOut.write(LPRCommand.STX.getCode());
      printerOut.writeBytes("1");
      printerOut.write(LPRCommand.LF.getCode());
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new LPRException("Error while start printing on the queue");
      }

      // Write control file
      printerOut.write(LPRCommand.STX.getCode());
      printerOut.writeBytes(String.valueOf(controlFile.length()));
      printerOut.write(LPRCommand.SPACE.getCode());
      printerOut.writeBytes("cfA" + jobId + user);
      printerOut.write(LPRCommand.LF.getCode());
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new LPRException("Error while start sending control file");
      }

      printerOut.writeBytes(controlFile);
      printerOut.write(LPRCommand.NULL.getCode());
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new LPRException("Error while sending control file");
      }

      // Write data file
      printerOut.write(LPRCommand.ETX.getCode());

      printerOut.writeBytes(String.valueOf(rawDocument.length));
      printerOut.write(LPRCommand.SPACE.getCode());
      printerOut.writeBytes("dfA" + jobId + user);
      printerOut.write(LPRCommand.LF.getCode());
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new LPRException("Error while start sending data file");
      }

      printerOut.write(rawDocument);
      printerOut.writeByte(0);
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new LPRException("Error while sending data file");
      }

      printerOut.flush();
      close(printerConnection);

      running = false;
      LOG.info("PrintJob Compleet!");

      // Trigger printSucceed event
      try
      {
        PrintEvent event = new PrintEvent(printer, document, this);
        for (PrinterListener listener : printer.getPrintListeners())
        {
          listener.printSucceed(event);
        }
      }
      catch (Throwable ee)
      {
        LOG.log(Level.SEVERE, "printSucceed event failed", ee);
      }
    }
    catch (Throwable e)
    {
      running = false;

      // Convert Throwable into LPRException
      LPRException lprException;
      if (e instanceof LPRException)
      {
        lprException = (LPRException) e;
      }
      else
      {
        lprException = new LPRException(e);
      }

      // Trigger printFailed event
      try
      {
        PrintEvent event = new PrintEvent(printer, document, this);
        for (PrinterListener listener : printer.getPrintListeners())
        {
          listener.printFailed(event, lprException);
        }
      }
      catch (Throwable ee)
      {
        LOG.log(Level.SEVERE, "printFailed event failed", ee);
      }

      // Throws Exception
      throw lprException;
    }
  }

  /**
   * Connect to printer
   * 
   * @return socket connection
   * @throws LPRException
   *           connection error
   */
  private Socket connect() throws LPRException
  {
    try
    {
      LOG.info("Connect to " + printer.getHost() + ":" + printer.getPort());
      Socket socket = new Socket(printer.getHost(), printer.getPort());
      socket.setSoTimeout(printer.getTimeout());
      return socket;
    }
    catch (IOException e)
    {
      throw new LPRException("Printer offline", e);
    }
  }

  /**
   * Close printer connection
   * 
   * @param socket
   *          printer connection
   */
  private void close(Socket socket)
  {
    try
    {
      socket.close();
    }
    catch (Exception e)
    {
    }
  }

  /**
   * Create control file
   * 
   * @return
   */
  private String createControlFile()
  {
    String controlFile = "";

    if (hostname != null)
    {
      controlFile += ("H" + hostname + "\n");
    }

    controlFile += ("P" + user + "\n");
    controlFile += ("J" + document.getDocumentName() + "\n");
    controlFile += ("L" + user + "\n");

    controlFile += ("UdfA" + jobId + hostname + "\n");

    for (int i = 0; i < document.getCopies(); i++)
    {
      controlFile += ("ldfA" + jobId + user + "\n");
    }

    controlFile += ("N" + document + "\n");

    return controlFile;
  }

  public boolean isRunning()
  {
    return running;
  }

  public LPRPrinter getPrinter()
  {
    return printer;
  }

  private String getNewJobId()
  {
    String data = String.valueOf((int) Math.floor(Math.random() * 999));
    int size = 3;
    String filler = "0";

    while ((data.length() < size))
    {
      data = filler + data;
    }
    return data;
  }

}
