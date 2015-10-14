package com.maxxton.printer;

import com.maxxton.printer.lpr.LPRDocument;
import com.maxxton.printer.lpr.LPRPrintJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * All data printed in this Job is kept in a buffer. This buffer can be used to
 * analyse
 * the data that is send to the printer.
 *
 * Copyright Maxxton 2015
 *
 * @author Hermans.S
 */
public class BufferedPrintJob extends LPRPrintJob
{

  private ByteArrayOutputStream buffer;
  private ByteArrayInputStream input;

  public BufferedPrintJob(Printer printer, LPRDocument doc)
  {
    super(printer, doc);
  }

  /**
   * Create fake connection
   *
   * @param port
   * @return Printer connection based on ByteArrayOutputStream
   * @throws PrintException
   */
  @Override
  protected PrinterConnection connect(int port) throws PrintException
  {
    buffer = new ByteArrayOutputStream();
    input = new ByteArrayInputStream(new byte[]
    {
      0, 0, 0, 0, 0, 0, 0
    });

    return new PrinterConnection(input, buffer);
  }

  /**
   * Return the printed buffer
   *
   * @return Bytes that should be send to the printer
   */
  public byte[] getBuffer()
  {
    return buffer.toByteArray();
  }

}
