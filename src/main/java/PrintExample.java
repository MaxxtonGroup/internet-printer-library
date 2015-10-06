
import com.maxxton.printer.PrintDocument;
import com.maxxton.printer.PrintException;
import com.maxxton.printer.Printer;
import com.maxxton.printer.PrintEvent;
import com.maxxton.printer.PrintFormat;
import com.maxxton.printer.PrintProtocol;
import com.maxxton.printer.PrinterListener;
import com.maxxton.printer.fgl.FGLCommand;
import com.maxxton.printer.fgl.FGLDocument;
import com.maxxton.printer.lpr.LPRDocument;
import java.io.IOException;

/**
 * This is an example for sending a print job to a network printer with lpr4java.
 * In this example the /bonexample file is printed on a receipt printer.
 * The next steps are done in order to accomplish this:
 * <ol>
 *   <li>
     Define the LPR printer: 
     Create a new Printer object and pass in the host name and optional the port number.
     The well known port for LPR is 515.
   </li>
 *   <li>
     Add attach a PrinterListener to the printer.
     With this listener you can catch the event when your LPRPrintJob is complete, or when it failed.
   </li>
 *   <li>
 *     Create a new LPRDocument.
 *     This document is for contains the data to be send to the printer.
 *     You can also define in the LPRDocument LPRCommands, 
 *     like Cutting the paper or changing the CharacterSet.
 *   </li>
 *   <li>
     Finally you can print the document. The printer creates a new LPRPrintJob 
     and schedule it so only one job can executed at the time for each printer.
   </li>
 * </ol>
 * 
 * 
 * @author Hermans.S
 * Copyright Maxxton 2015
 */
public class PrintExample
{

  public static void main(String[] args) throws IOException
  {

    String host = "192.168.252.145";

    // Create printer
    Printer printer = new Printer(host);

    // Create document
//    PrintDocument document = new PrintDocument("test bon");
//    PrintFormat format = new PrintFormat(PrintExample.class.getResourceAsStream("/bonexample"), "world");
//    document.insert(format);
    
    FGLDocument document = new FGLDocument("test bon");
    document.insert(FGLCommand.ROTATE_TEXT_270_DEGREES);
    document.insertFont(3);
    document.insertRowAndColumn(621,40);
    document.insertFontHeightWidht(1, 1);
    
    document.insert("Hello world");
    document.insert(FGLCommand.DOCUMENT_END);
    
//    LPRDocument document = new LPRDocument("test bon");
//    document.insert("Hello world");
//    document.insertPaperCut();
//    
    printer.print(document, PrintProtocol.SIMPLE_LPR);
    
    
    System.out.println(new String(document.getRaw()));

    // Print document
//    printer.print(document, PrintProtocol.LPR);
  }

}
