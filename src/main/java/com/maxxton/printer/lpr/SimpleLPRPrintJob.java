package com.maxxton.printer.lpr;

import com.maxxton.printer.PrintDocument;
import com.maxxton.printer.Printer;
import com.maxxton.printer.PrinterConnection;

/**
 * Executes simplified version of LPRPrintJob <br>
 * 1. Open connection with the Printer <br>
 * 2. Send request for new printjob <br>
 * 3. Send data file <br>
 * 4. Close connection
 *
 * @author Hermans.S Copyright Maxxton 2015
 */
public class SimpleLPRPrintJob extends LPRPrintJob
{

  public SimpleLPRPrintJob(Printer printer, PrintDocument document)
  {
    super(printer, document);
  }

  /**
   * Print the document
   *
   * @param printerConnection Connection with the printer
   * @throws Exception
   */
  @Override
  public void execute(PrinterConnection printerConnection) throws Exception
  {
    // Create document
    byte[] rawDocument = getDocument().getRaw();

    //Send lpr printjob
    sendHeader(printerConnection);
    sendDataFile(printerConnection, rawDocument, false);
  }

}
