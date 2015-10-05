
/**
 * 
 * Ticket implementation of a boca template
 * 
 * @author R. Jharia [r.jharia@maxxton.com]
 * 
 */
public class BocaTicket implements Constants
{
  // For Portrait print out
  private String _orientation = BOTTOM_TO_TOP;
  // For Landscape print out. use:
  //private String _orientation = BocaConfiguration.LEFT_TO_RIGHT;
  private int _fontSize  = FONT_SIZE;
  private int _maxColumn = new Long(Math.round(BocaTicket.PAPER_HEIGHT * 0.39 * BocaTicket.PRINTER_DPI)).intValue();
  private int _maxRow    = new Long(Math.round(BocaTicket.PAPER_WIDTH * 0.39 * BocaTicket.PRINTER_DPI)).intValue();
  private int _rowLength    = _maxColumn / BocaTicket.FONT_WIDTH[_fontSize -1];
  private int _colLength    = _maxColumn / BocaTicket.FONT_HEIGHT[_fontSize -1];

  // use default values to initialize these configuration variables
  private String _font = FONT;
  // This is the Box used for the characters as per the font used.
  private int[] _fontWidth = FONT_WIDTH;
  private int[] _fontHeight = FONT_HEIGHT;
  private double _paperWidth = PAPER_WIDTH;
  private double _paperHeight = PAPER_HEIGHT;
  private int _spaceBetweenRows = SPACEBETWEENROW;
  private int _spaceBetweenColumns = SPACEBETWEENCOLUMN;
  private int _rowSize = ROW_SIZE;

  private String _customerName = null;
  private String _resortName = null;
  private String _reservationNumber = null;
  private String _arrivalDate = null;
  private String _departureDate = null;

  private String _templateContent;

  // Default constructor : Creates Boca ticket with Default settings
  public BocaTicket()
  {
  }

  public BocaTicket (String font, int[] fontWidth, int[] fontHeight, double paperWidth, double paperHeight, int spaceBetweenRows, int spaceBetweenColumns, int rowSize )
  {
    _font = font;
    _fontWidth = fontWidth;
    _fontHeight = fontHeight;
    _paperWidth = paperWidth;
    _paperHeight = paperHeight;
    _spaceBetweenRows = spaceBetweenRows;
    _spaceBetweenColumns = spaceBetweenColumns;
    _rowSize = rowSize;
  }

  public void setFont(String font)
  {
    this._font = font;
  }

  public String getFont()
  {
    if(_fontSize <= 0 || _fontSize > 13)
    {
      _fontSize = FONT_SIZE;
      return "<F"+_fontSize+">";
    }
    else return _font;
  }

  public void setFontWidth(int[] fontWidth)
  {
    this._fontWidth = fontWidth;
  }

  public int[] getFontWidth()
  {
    return _fontWidth;
  }

  public void setFontHeight(int[] fontHeight)
  {
    this._fontHeight = fontHeight;
  }

  public int[] getFontHeight()
  {
    return _fontHeight;
  }

  public void setPaperWidth(double paperWidth)
  {
    this._paperWidth = paperWidth;
  }

  public double getPaperWidth()
  {
    return _paperWidth;
  }

  public void setPaperHeight(double paperHeight)
  {
    this._paperHeight = paperHeight;
  }

  public double getPaperHeight()
  {
    return _paperHeight;
  }

  public void setSpaceBetweenRows(int spaceBetweenRows)
  {
    this._spaceBetweenRows = spaceBetweenRows;
  }

  public int getSpaceBetweenRows()
  {
    return _spaceBetweenRows;
  }

  public void setSpaceBetweenColumns(int spaceBetweenColumns)
  {
    this._spaceBetweenColumns = spaceBetweenColumns;
  }

  public int getSpaceBetweenColumns()
  {
    return _spaceBetweenColumns;
  }

  public void setRowSize(int rowSize)
  {
    this._rowSize = rowSize;
  }

  public int getRowSize()
  {
    return _rowSize;
  }

  public void setMaxColumn(int maxColumn)
  {
    this._maxColumn = maxColumn;
  }

  public int getMaxColumn()
  {
    return _maxColumn;
  }

  public void setMaxRow(int maxRow)
  {
    this._maxRow = maxRow;
  }

  public int getMaxRow()
  {
    return _maxRow;
  }

  public void setRowLength(int rowLength)
  {
    this._rowLength = rowLength;
  }

  public int getRowLength()
  {
    return _rowLength;
  }

  public void setColLength(int colLength)
  {
    this._colLength = colLength;
  }

  public int getColLength()
  {
    return _colLength;
  }

  public void setCustomerName(String customerName)
  {
    this._customerName = customerName;
  }

  public String getCustomerName()
  {
    return _customerName;
  }

  public void setResortName(String resortName)
  {
    this._resortName = resortName;
  }

  public String getResortName()
  {
    return _resortName;
  }

  public void setReservationNumber(String reservationNumber)
  {
    this._reservationNumber = reservationNumber;
  }

  public String getReservationNumber()
  {
    return _reservationNumber;
  }

  public void setArrivalDate(String arrivalDate)
  {
    this._arrivalDate = arrivalDate;
  }

  public String getArrivalDate()
  {
    return _arrivalDate;
  }

  public void setDepartureDate(String departureDate)
  {
    this._departureDate = departureDate;
  }

  public String getDepartureDate()
  {
    return _departureDate;
  }

  public void setTemplateContent(String templateContent)
  {
    this._templateContent = templateContent;
  }

  public String getTemplateContent()
  {
    return _templateContent;
  }

  public void setOrientation(String orientation)
  {
    this._orientation = orientation;
  }

  public String getOrientation()
  {
    return _orientation;
  }
}