package com.maxxton.printer;

/**
 * Event for when the printer is done printing.
 *
 * @author Hermans.S Copyright Maxxton 2015
 */
public class PrintEvent
{

  private final Printer printer;
  private final PrintDocument document;
  private final PrintJob printJob;

  public PrintEvent(Printer printer, PrintDocument document, PrintJob printJob)
  {
    this.printer = printer;
    this.document = document;
    this.printJob = printJob;

  }

  public Printer getPrinter()
  {
    return printer;
  }

  public PrintDocument getDocument()
  {
    return document;
  }

  public PrintJob getPrintJob()
  {
    return printJob;
  }

}
