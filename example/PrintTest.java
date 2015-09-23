
import com.maxxton.printer.lpr.CharacterSet;
import com.maxxton.printer.lpr.LPRDocument;
import com.maxxton.printer.lpr.LPRException;
import com.maxxton.printer.lpr.LPRPrinter;
import com.maxxton.printer.lpr.PrintEvent;
import com.maxxton.printer.lpr.PrinterListener;
import java.io.IOException;

/**
 *
 * @author hermans.s
 */
public class PrintTest {

    public static void main(String[] args) throws IOException {

//        String host = "[ip address]";
        String host = "192.168.252.17";
        int port = 515; // 515 is the well known port for LPR

        //Create printer
        LPRPrinter printer = new LPRPrinter(host, port);

        //Install listener
        printer.addPrintListener(new PrinterListener() {

            @Override
            public void printSucceed(PrintEvent event) {
                System.out.println("Succeed");
            }

            @Override
            public void printFailed(PrintEvent event, LPRException e) {
                System.err.println("Failed");
            }
        });

        //Create document
        LPRDocument document = new LPRDocument("Bonnetjes test");
        //Set characterset to STANDARD_EUROPE_USA
        document.insertCharacterset(CharacterSet.STANDARD_EUROPE_USA);
        //Insert bon example
        document.insert(PrintTest.class.getResourceAsStream("/bonexample"));
        //Cut the paper at the end
        document.insertPaperCut();

        //Print document
        printer.print(document);
    }

}
