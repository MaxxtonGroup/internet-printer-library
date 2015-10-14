package com.maxxton.printer.fgl;

import com.maxxton.printer.PrintDocument;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * PrintDocument is the document to be printed. This document is build up with FGLCommands
 * and text. These commands and text are send to the printer in a PrintJob.
 *
 * Copyright Maxxton 2015
 *
 * @author hermans.s
 */
public class FGLDocument extends PrintDocument
{

  public static final int DEFAULT_FONT = 3;
  public static final int DEFAULT_FONT_HEIGHT = 1;
  public static final int DEFAULT_FONT_WIDTH = 1;
  public static final FGLCommand DEFAULT_ORIENTATION = FGLCommand.TEXT_ORIENTATION_PORTRET;

  public static final int PRINTER_DPI = 200;
  public static final double PAPER_WIDTH = 8.2;
  public static final double PAPER_HEIGHT = 15.2;

  public static final int SPACE_BETWEEN_ROW = 40;
  public static final int SPACE_BETWEEN_COLUMN = 40;

  public static final int ROW_SIZE = 30;
  public static final int FONT_SIZE = 3;

  public static final int[] FONT_WIDTHS =
  {
    7, 10, 20, 7, 7, 34, 20, 20, 13, 28, 26, 47, 20
  };
  public static final int[] FONT_HEIGHTS =
  {
    8, 18, 33, 11, 12, 56, 31, 33, 22, 41, 49, 91, 42
  };

  private final int font;
  private final int fontHeight;
  private final int fontWidth;
  private final FGLCommand orientation;

  private final int maxColumns;
  private final int maxRows;
  private final int rowLength;

  public FGLDocument(String documentName)
  {
    this(documentName, DEFAULT_FONT, DEFAULT_FONT_HEIGHT, DEFAULT_FONT_WIDTH, DEFAULT_ORIENTATION);
  }

  public FGLDocument(String documentName, int font, int fontHeight, int fontWidth, FGLCommand orientation)
  {
    super(documentName);
    this.font = font;
    this.fontHeight = fontHeight;
    this.fontWidth = fontWidth;
    this.orientation = orientation;

    maxColumns = new Long(Math.round(PAPER_HEIGHT * 0.39 * PRINTER_DPI)).intValue();
    maxRows = new Long(Math.round(PAPER_WIDTH * 0.39 * PRINTER_DPI)).intValue();
    rowLength = maxColumns / FONT_WIDTHS[font - 1];
  }

  public static void main(String[] args)
  {
    FGLDocument doc = new FGLDocument("hello");
    System.out.println(doc.maxColumns);
    System.out.println(doc.maxRows);
    System.out.println(doc.rowLength);
  }

  public void insert(FGLCommand command, String... args)
  {
    insert("<" + command.getCode());
    for (int i = 0; i < args.length; i++)
    {
      if (i > 0)
      {
        insert(",");
      }
      insert(args[i]);
    }
    insert(">");
  }

  private void insertRowAndColumn(int row, int column)
  {
    insert(FGLCommand.ROW_COLUMN, String.valueOf(row), String.valueOf(column));
  }

  private void insertFontHeightWidht(int height, int width)
  {
    insert(FGLCommand.FONT_HEIGHT_WIDTH, String.valueOf(height), String.valueOf(width));
  }

  private void insertFont(int font)
  {
    FGLCommand fontCommand;
    switch (font)
    {
      case 1:
        fontCommand = FGLCommand.FONT_1;
        break;
      case 2:
        fontCommand = FGLCommand.FONT_2;
        break;
      case 3:
        fontCommand = FGLCommand.FONT_3;
        break;
      case 4:
        fontCommand = FGLCommand.FONT_4;
        break;
      case 5:
        fontCommand = FGLCommand.FONT_5;
        break;
      case 6:
        fontCommand = FGLCommand.FONT_6;
        break;
      case 7:
        fontCommand = FGLCommand.FONT_7;
        break;
      case 8:
        fontCommand = FGLCommand.FONT_8;
        break;
      case 9:
        fontCommand = FGLCommand.FONT_9;
        break;
      case 10:
        fontCommand = FGLCommand.FONT_10;
        break;
      case 11:
        fontCommand = FGLCommand.FONT_11;
        break;
      case 12:
        fontCommand = FGLCommand.FONT_12;
        break;
      case 13:
        fontCommand = FGLCommand.FONT_13;
        break;
      default:
        throw new IllegalStateException("Font '" + font + "' not supported");
    }
    insert(fontCommand);
  }

  @Override
  public void insertLineFeed()
  {
    insert(FGLCommand.NEW_LINE);
  }

  public void end()
  {
    insert(FGLCommand.DOCUMENT_END);
  }

  private ArrayList<String> splitLargeLines(String line)
  {
    StringTokenizer tokenizer = new StringTokenizer(line);
    ArrayList<String> lines = new ArrayList();
    ArrayList<String> out = new ArrayList();
    while (tokenizer.hasMoreTokens())
    {
      
    }
    return out;
  }

  public byte[] getRaw()
  {
    int maxLength = (orientation.equals(FGLCommand.TEXT_ORIENTATION_PORTRET)
            || orientation.equals(FGLCommand.TEXT_ORIENTATION_PORTRET_INVERTED)
                    ? maxColumns : maxRows);
    String content = new String(super.getRaw());
    String[] tickets = content.split("<p>");
    for (String ticket : tickets)
    {
      ArrayList<String> rows = new ArrayList();
      String[] lines = ticket.split("\n");
      for (String line : lines)
      {

      }

    }
    return super.getRaw();

  }

}
