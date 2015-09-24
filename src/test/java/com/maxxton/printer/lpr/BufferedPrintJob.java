
package com.maxxton.printer.lpr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * All data printed in this Job is kept in a buffer.
 * This buffer can be used to analyse the data that is send to the printer.
 * 
 * @author Hermans.S
 * @copyright Maxxton 2015
 */
public class BufferedPrintJob extends PrintJob
{
  
  private ByteArrayOutputStream buffer;
  private ByteArrayInputStream input;
  
  public BufferedPrintJob(LPRPrinter printer, LPRDocument doc){
    super(printer, doc);
  }

  /**
   * Create fake connection
   * @return Printer connection based on ByteArrayOutputStream
   * @throws LPRException 
   */
  @Override
  protected PrinterConnection connect() throws LPRException
  {
    buffer = new ByteArrayOutputStream();
    input = new ByteArrayInputStream(new byte[]{0,0,0,0,0,0,0});
    
    return new PrinterConnection(input, buffer);
  }
  
  /**
   * Return the printed buffer
   * @return Bytes that should be send to the printer
   */
  public byte[] getBuffer(){
    return buffer.toByteArray();
  }
  
}
