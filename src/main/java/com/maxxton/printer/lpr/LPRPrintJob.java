
package com.maxxton.printer.lpr;

import com.maxxton.printer.PrintDocument;
import com.maxxton.printer.PrintException;
import com.maxxton.printer.PrintJob;
import com.maxxton.printer.PrintProtocol;
import com.maxxton.printer.PrinterConnection;
import com.maxxton.printer.Printer;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Executes LPRPrintJob <br>
 * 1. Open connection with the Printer <br>
 * 2. Send control file <br>
 * 3. Send data file <br>
 * 4. Close connection
 * 
 * @author Hermans.S
 * Copyright Maxxton 2015
 */
public class LPRPrintJob extends PrintJob{

  private final String jobId;
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
  public LPRPrintJob(Printer printer, PrintDocument document)
  {
    super(printer, document, PrintProtocol.LPR);
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
   * @param printerConnection Connection with the printer
   * @throws PrintException
   */
  @Override
  public void execute(PrinterConnection printerConnection) throws Exception
  {
      BufferedReader printerIn = new BufferedReader(new InputStreamReader(printerConnection.getInputStream()));
      DataOutputStream printerOut = new DataOutputStream(printerConnection.getOutputStream());

      // Create control file
      String controlFile = createControlFile();

      // Create document
      byte[] rawDocument = getDocument().getRaw();

      // start sending
      printerOut.write(02);
      printerOut.writeBytes("1");
      printerOut.write(LPRCommand.LF.getCodes());
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new PrintException("Error while start printing on the queue");
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
        throw new PrintException("Error while start sending control file");
      }

      System.out.println(controlFile);
      printerOut.writeBytes(controlFile);
      printerOut.write(LPRCommand.NULL.getCodes());
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new PrintException("Error while sending control file");
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
        throw new PrintException("Error while start sending data file");
      }

      printerOut.write(rawDocument);
      printerOut.writeByte(0);
      printerOut.flush();

      if (printerIn.read() != 0)
      {
        throw new PrintException("Error while sending data file");
      }

      printerOut.flush();
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
    controlFile += ("J" + getDocument().getDocumentName() + "\n");
    controlFile += ("L" + user + "\n");

    controlFile += ("UdfA" + jobId + hostname + "\n");

    for (int i = 0; i < getDocument().getCopies(); i++)
    {
      controlFile += ("ldfA" + jobId + user + "\n");
    }

    controlFile += ("N" + getDocument().getDocumentName() + "\n");

    return controlFile;
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
