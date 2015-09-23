
package com.maxxton.printer.lpr;

/**
 *
 * @author hermans.s
 */
public class PrintEvent{
    
    private final LPRPrinter printer;
    private final LPRDocument document;
    private final IPrintJob printJob;
    
    public PrintEvent(LPRPrinter printer, LPRDocument document, IPrintJob printJob){
        this.printer = printer;
        this.document = document;
        this.printJob = printJob;
        
    }

    public LPRPrinter getPrinter() {
        return printer;
    }

    public LPRDocument getDocument() {
        return document;
    }

    public IPrintJob getPrintJob() {
        return printJob;
    }
    
}
