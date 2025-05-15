package com.maxxton.printer.lpr;

import com.maxxton.printer.PrintException;
import com.maxxton.printer.Printer;
import com.maxxton.printer.PrinterConnection;

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
public class BufferedSimpleLPRPrintJob extends SimpleLPRPrintJob {

  private ByteArrayOutputStream buffer;
  private ByteArrayInputStream input;

  public BufferedSimpleLPRPrintJob(Printer printer, LPRDocument doc) {
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
  protected PrinterConnection connect(int port) throws PrintException {
    buffer = new ByteArrayOutputStream();
    input = new ByteArrayInputStream(new byte[] { 0, 0, 0, 0, 0, 0, 0 });

    return new PrinterConnection(input, buffer);
  }

  /**
   * Return the printed buffer
   *
   * @return Bytes that should be send to the printer
   */
  public byte[] getBuffer() {
    return buffer.toByteArray();
  }

}
