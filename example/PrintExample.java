
import com.maxxton.printer.lpr.CharacterSet;
import com.maxxton.printer.lpr.LPRDocument;
import com.maxxton.printer.lpr.LPRException;
import com.maxxton.printer.lpr.LPRPrinter;
import com.maxxton.printer.lpr.PrintEvent;
import com.maxxton.printer.lpr.PrinterListener;
import java.io.IOException;

/**
 * This is an example for sending a print job to a network printer with lpr4java.
 * In this example the /bonexample file is printed on a receipt printer.
 * The next steps are done in order to accomplish this:
 * <ol>
 *   <li>
 *     Define the LPR printer: 
 *     Create a new LPRPrinter object and pass in the host name and optional the port number.
 *     The well known port for LPR is 515.
 *   </li>
 *   <li>
 *     Add attach a PrinterListener to the printer.
 *     With this listener you can catch the event when your PrintJob is complete, or when it failed.
 *   </li>
 *   <li>
 *     Create a new LPRDocument.
 *     This document is for contains the data to be send to the printer.
 *     You can also define in the LPRDocument LPRCommands, 
 *     like Cutting the paper or changing the CharacterSet.
 *   </li>
 *   <li>
 *     Finally you can print the document. The printer creates a new PrintJob 
 *     and schedule it so only one job can executed at the time for each printer.
 *   </li>
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

    String host = "192.168.252.17";
    int port = 515; // 515 is the well known port for LPR

    // Create printer
    LPRPrinter printer = new LPRPrinter(host, port);

    // Install listener
    printer.addPrintListener(new PrinterListener()
    {

      @Override
      public void printSucceed(PrintEvent event)
      {
        System.out.println("Succeed");
      }

      @Override
      public void printFailed(PrintEvent event, LPRException e)
      {
        System.err.println("Failed");
      }
    });

    // Create document
    LPRDocument document = new LPRDocument("bon test");
    // Set characterset to STANDARD_EUROPE_USA
    document.insertCharacterset(CharacterSet.STANDARD_EUROPE_USA);
    // Insert bon example
    document.insert(PrintExample.class.getResourceAsStream("/bonexample"));
    // Cut the paper at the end
    document.insertPaperCut();

    // Print document
    printer.print(document);
  }

}
