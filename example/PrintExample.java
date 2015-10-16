
import com.maxxton.printer.*;
import com.maxxton.printer.lpr.*;
import java.io.IOException;

/**
 * This is an example for sending a LPR print job to a network printer. In this
 * example the /bonexample file is printed on a receipt printer. The next steps
 * are done
 * in order to accomplish this:
 * <ol>
 * <li>
 * Define the LPR printer: Create a new LPRPrinter object and pass in the host
 * name and
 * optional the port number. The well known port for LPR is 515.
 * </li>
 * <li>
 * Add attach a PrinterListener to the printer. With this listener you can catch
 * the event
 * when your PrintJob is complete, or when it failed.
 * </li>
 * <li>
 * Create a new LPRDocument. This document is for contains the data to be send
 * to the
 * printer. You can also define in the LPRDocument LPRCommands, like Cutting the
 * paper or
 * changing the CharacterSet.
 * </li>
 * <li>
 * Finally you can print the document. The printer creates a new PrintJob and
 * schedule it
 * so only one job can executed at the time for each printer.
 * </li>
 * </ol>
 * 
 * Copyright Maxxton 2015
 *
 * @author Hermans.S
 */
public class PrintRawExample
{

  public static void main(String[] args) throws IOException
  {

    String host = "192.168.252.17";

    // Create printer
    Printer printer = new Printer(host);

    // Create document
    LPRDocument document = new LPRDocument("bon test");
    // Set characterset to STANDARD_EUROPE_USA
    document.insertCharacterset(CharacterSet.STANDARD_EUROPE_USA);
    // Insert bon example
    document.insert(PrintExample.class.getResourceAsStream("/bonexample"));
    // Cut the paper at the end
    document.insertDocumentEnd();

    // Print document
    printer.print(document, PrintProtocol.LPR);
  }

}
