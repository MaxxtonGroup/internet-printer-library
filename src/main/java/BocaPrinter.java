

import java.util.ArrayList;
import java.net.Socket;
import java.io.OutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class BocaPrinter implements Constants
{
  private BocaTicket _bocaTicket = null;
  private String _ipAddress;
  private int _currentRow = 0;
  private int _currentColumn = 0;
  private int _port  = PORT;
  private int _printerDpi = 200;
  private static OutputStream _out = null;
  private Socket _socket;
  private static final Logger LOGGER = Logger.getLogger(BocaPrinter.class.getName());
  
  public BocaPrinter(String ipAddress)
  {
    this(null, ipAddress);
  }

  public BocaPrinter(BocaTicket bocaTicket, String ipAddress)
  {
    _ipAddress = ipAddress;
    _bocaTicket = bocaTicket;

    openSocket();
  }

  public BocaPrinter(String ipAddress, int port, String font, int printerDpi, int[] fontWidth, int[] fontHeight, double paperWidth, double paperHeight, int spaceBetweenRows, int spaceBetweenColumns, int rowSize )
  {
    _ipAddress = ipAddress;
    _port = port;
    _printerDpi = printerDpi;
    _bocaTicket = new BocaTicket(font, fontWidth, fontHeight, paperWidth, paperHeight, spaceBetweenRows, spaceBetweenColumns, rowSize);

    openSocket();
  }

  private void openSocket()
  {
    try
    {
      _socket = new Socket(_ipAddress, _port);
      _out = _socket.getOutputStream();
    }
    catch (IOException e)
    {
      // printer not reachable
      LOGGER.log(Level.SEVERE, "error in method openSocket "+e.getMessage(),e);  
      throw new RuntimeException("libPrinter", e);
    }
  }

  public void closeSocket()
  {
    try
    {
      _out.flush();
      _out.close();
      _socket.close();
    }
    catch (Exception e)
    {
      LOGGER.log(Level.SEVERE, "error in method closeSocket "+e.getMessage(),e); 
      // doesnt matter
    }
  }

  public BocaTicket getBocaTicket()
  {
    return _bocaTicket;
  }

  public void setBocaTicket(BocaTicket ticket)
  {
    _bocaTicket = ticket;
  }

  public boolean print()
  {
    initRowColumn();
    // parse the Template with the provided values
    String printString = parseTemplateString();
    printCard(printString);
    return true;
  }

  private void initRowColumn()
  {
    _currentRow = 0;
    _currentColumn = 0;
  }
  private void printCard(String printString)
  {
    if(_bocaTicket.getOrientation().equals(BOTTOM_TO_TOP))
    {
      _bocaTicket.setRowLength(_bocaTicket.getColLength());
      _currentRow = new Long(Math.round(_bocaTicket.getPaperHeight() * 0.39 * _printerDpi)).intValue();
    }
    _currentRow = 621;

    String printLine[] = printString.split("\n");
    for(String line : printLine)
    {
      ArrayList<String> splitedDataArray = splitLargeLines(line);
      printSplittedArrayList(splitedDataArray);
    }
    printCardData(printCutTicket());
  }

  public void printSplittedArrayList(ArrayList<String> splitedData)
  {
    for(String textLine : splitedData)
    {
      updateRowColumnSpace();
      printCardData(_bocaTicket.getOrientation() + _bocaTicket.getFont() + getRowColumn(_currentRow, _currentColumn) +
                    getDefaultHeightWidth() + textLine);

    }
  }

  public void updateRowColumnSpace()
  {
    if(_bocaTicket.getOrientation().equals(BOTTOM_TO_TOP)) _currentColumn += _bocaTicket.getSpaceBetweenColumns();
    if(_bocaTicket.getOrientation().equals(LEFT_TO_RIGHT)) _currentRow += _bocaTicket.getSpaceBetweenRows();
  }

  public void printCardData(String text)
  {
    try
    {
      _out.write(text.getBytes());
    }
    catch(IOException ex)
    {
      LOGGER.log(Level.SEVERE, "error in method printCardData "+ex.getMessage(),ex);  
      throw new RuntimeException("libPrinter", ex);
    }
  }

  public ArrayList<String> splitLargeLines(String splitString)
  {
    StringTokenizer tokenizer = new StringTokenizer(splitString, " ");
    ArrayList<String> strArrayList = new ArrayList<String>();
    ArrayList<String> finalArrayList = new ArrayList<String>();
    int strCount = 0;
    String stringToPrint ="";

    while(tokenizer.hasMoreTokens())
    {
      strArrayList.add(tokenizer.nextToken());
    }

    for(int count=0; count < strArrayList.size(); count++)
    {
      String testString = strArrayList.get(count);
      if((testString.length() + 1) > _bocaTicket.getRowLength())
      {
        throw new RuntimeException("error.template.wordlength.exceeded");
      }
      strCount += testString.length() + 1;
      if(strCount >= _bocaTicket.getRowLength())
      {
        finalArrayList.add(stringToPrint);
        strCount = 0;
        stringToPrint = "";
        count--;
      }
      else
      {
        stringToPrint += " " + testString;
      }
    }
    finalArrayList.add(stringToPrint);
    return finalArrayList;
  }

  private String parseTemplateString()
  {
    StringWriter writer = new StringWriter();
    VelocityContext _ctx = null;
    try
    {
      Velocity.init();
      _ctx = new VelocityContext();
      // assign values which will be parsed into the String
      _ctx.put("CustomerName", _bocaTicket.getCustomerName());
      _ctx.put("ResortName", _bocaTicket.getResortName());
      _ctx.put("ReservationNumber", _bocaTicket.getReservationNumber());
      _ctx.put("ArrivalDate", _bocaTicket.getArrivalDate());
      _ctx.put("DepartureDate", _bocaTicket.getDepartureDate());

      Velocity.evaluate( _ctx, writer, "BocaPrint",_bocaTicket.getTemplateContent());
    }
    catch (Exception e)
    {
      LOGGER.log(Level.SEVERE, "error in method parseTemplateString "+e.getMessage(),e);  
      throw new RuntimeException("libPrinter", e);
    }
    return writer.toString();
  }

  public String getRowColumn(int row, int column)
  {
    return "<RC"+row+","+column+">";
  }

  public String getDefaultHeightWidth()
  {
    return "<HW1,1>";
  }

  public String addNewRow()
  {
    return "<NR>";
  }

  public String getBoxSize(int height, int width)
  {
    return "<BS"+height+","+width+">";
  }

  public String printCutTicket()
  {
    return "<p>";
  }

  public String printDontCutTicket()
  {
    return "<q>";
  }

  public String getHeightWidth(int height, int width)
  {
    return "<HW"+height+","+width+">";
  }

  public String statusRequest()
  {
    return "<S1>";
  }

  public String repeatPrint(int copies)
  {
    return "<RE"+copies+">";
  }

  public void setIpAddress(String ipAddress)
  {
    this._ipAddress = ipAddress;
  }

  public String getIpAddress()
  {
    return _ipAddress;
  }

  public void setCurrentRow(int currentRow)
  {
    this._currentRow = currentRow;
  }

  public int getCurrentRow()
  {
    return _currentRow;
  }

  public void setCurrentColumn(int currentColumn)
  {
    this._currentColumn = currentColumn;
  }

  public int getCurrentColumn()
  {
    return _currentColumn;
  }

  public void setPort(int port)
  {
    this._port = port;
  }

  public int getPort()
  {
    return _port;
  }

  public void setPrinterDpi(int printerDpi)
  {
    this._printerDpi = printerDpi;
  }

  public int getPrinterDpi()
  {
    return _printerDpi;
  }
}