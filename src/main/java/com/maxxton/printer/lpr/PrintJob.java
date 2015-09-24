
package com.maxxton.printer.lpr;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executes PrintJob <br>
 * 1. Open connection with the LPRPrinter <br>
 * 2. Send control file <br>
 * 3. Send data file <br>
 * 4. Close connection
 * 
 * @author Hermans.S
 * @copyright Maxxton 2015
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
      PrinterConnection printerConnection = connect();
      BufferedReader printerIn = new BufferedReader(new InputStreamReader(printerConnection.getInputStream()));
      DataOutputStream printerOut = new DataOutputStream(printerConnection.getOutputStream());

      // Create control file
      String controlFile = createControlFile();

      // Create document
      byte[] rawDocument = document.getRaw();

      // start sending
      printerOut.write(02);
      printerOut.writeBytes("1");
      printerOut.write(LPRCommand.LF.getCodes());
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new LPRException("Error while start printing on the queue");
      }

      // Write control file
      printerOut.write(02);
      printerOut.writeBytes(String.valueOf(controlFile.length()));
      printerOut.write(LPRCommand.SPACE.getCodes());
      printerOut.writeBytes("cfA" + jobId + hostname);
      printerOut.write(LPRCommand.LF.getCodes());
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new LPRException("Error while start sending control file");
      }

      printerOut.writeBytes(controlFile);
      printerOut.write(LPRCommand.NULL.getCodes());
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new LPRException("Error while sending control file");
      }

      // Write data file
      printerOut.write(03);

      printerOut.writeBytes(String.valueOf(rawDocument.length));
      printerOut.write(LPRCommand.SPACE.getCodes());
      printerOut.writeBytes("dfA" + jobId + hostname);
      printerOut.write(LPRCommand.LF.getCodes());
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
   * @return printer connection
   * @throws LPRException
   *           connection error
   */
  protected PrinterConnection connect() throws LPRException
  {
    try
    {
      LOG.info("Connect to " + printer.getHost() + ":" + printer.getPort());
      Socket socket = new Socket(printer.getHost(), printer.getPort());
      socket.setSoTimeout(printer.getTimeout());
      return new PrinterConnection(socket);
    }
    catch (IOException e)
    {
      throw new LPRException("Printer offline", e);
    }
  }

  /**
   * Close printer connection
   * 
   * @param connection
   *          printer connection
   */
  protected void close(PrinterConnection connection)
  {
    try
    {
      connection.close();
    }
    catch (Exception e)
    {
    }
  }

  /**
   * Create control file
   * 
   * @return control file
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

    controlFile += ("N" + document.getDocumentName() + "\n");

    return controlFile;
  }

  @Override
  public boolean isRunning()
  {
    return running;
  }

  @Override
  public LPRPrinter getPrinter()
  {
    return printer;
  }
  
  public int getJobId(){
    return Integer.parseInt(jobId);
  }
  
  public String getUser(){
    return user;
  }
  
  public String getHostname(){
    return hostname;
  }

  /**
   * Get new random job id
   * @return number between 000-999
   */
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
