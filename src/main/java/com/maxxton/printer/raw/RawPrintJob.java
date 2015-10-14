package com.maxxton.printer.raw;

import com.maxxton.printer.PrintDocument;
import com.maxxton.printer.PrintException;
import com.maxxton.printer.PrintJob;
import com.maxxton.printer.PrintProtocol;
import com.maxxton.printer.Printer;
import com.maxxton.printer.PrinterConnection;
import java.io.DataOutputStream;

/**
 *
 * @author hermans.s
 */
public class RawPrintJob extends PrintJob
{

  public RawPrintJob(Printer printer, PrintDocument document)
  {
    super(printer, document, PrintProtocol.RAW);
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
    DataOutputStream printerOut = new DataOutputStream(printerConnection.getOutputStream());
    printerOut.write(getDocument().getRaw());
    printerOut.flush();
  }

}
