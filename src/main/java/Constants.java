
public interface Constants
{
  public static final String I18N_ID = "libPrinter";
  
  // default values for the printer print configuration
  public static final int PORT = 9100;
  public static final String FONT = "<F3>";
  public static final int PRINTER_DPI  = 200;

  // This is the Box used for the characters as per the font used.
  public static final int[] FONT_WIDTH  = {7,10,20,7,7,34,20,20,13,28,26,47,20};
  public static final int[] FONT_HEIGHT = {8,18,33,11,12,56,31,33,22,41,49,91,42};

  // size is mentioned in centimeters
  //public static double PAPER_WIDTH = 5.4;
  //public static double PAPER_HEIGHT = 8.2;

  public static final double PAPER_WIDTH  = 8.2;
  public static final double PAPER_HEIGHT = 15.2;

  public static final int SPACEBETWEENROW = 40;
  public static final int SPACEBETWEENCOLUMN = 40; 
  public static final int ROW_SIZE = 30;
  public static final int FONT_SIZE = 3;
  
  // Non-rotated (NR) : characters are printed across the ticket from left to right
  public static String LEFT_TO_RIGHT = "<NR>";

  // Rotated right (RR) : characters are printed down the ticket from top to bottom.
  public static String TOP_TO_BOTTOM = "<RR>";

  // Rotated upside down (RU) characters are printed across the ticket from right to left.
  public static String RIGHT_TO_LEFT = "<RU>";

  // Rotated left (RL) characters are printed up the ticket from bottom to the top.
  public static String BOTTOM_TO_TOP = "<RL>";
}