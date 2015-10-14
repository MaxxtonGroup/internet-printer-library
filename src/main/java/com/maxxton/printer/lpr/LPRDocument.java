package com.maxxton.printer.lpr;

import com.maxxton.printer.PrintDocument;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * PrintDocument is the document to be printed. This document is build up with LPRCommands
 * and text. These commands and text are send to the printer in a PrintJob.
 *
 * @author Hermans.S Copyright Maxxton 2015
 */
public class LPRDocument extends PrintDocument
{

  private CharacterSet charset = CharacterSet.NONE;

  public LPRDocument(String documentName)
  {
    super(documentName);
  }

  /**
   * Insert text as string
   *
   * @param string text
   */
  @Override
  public void insert(String string)
  {
    byte[] bytes = string.getBytes(charset.getCharset());
    insert(bytes);
  }

  /**
   * Insert LPRCommands
   *
   * @param commands LPRCommand
   */
  public void insert(LPRCommand... commands)
  {
    for (LPRCommand command : commands)
    {
      insert(command.getCodes());
    }
  }

  /**
   * Insert tab
   */
  public void insertTab()
  {
    insert(LPRCommand.HT);
  }

  /**
   * Change the CharacterSet for the following data to be inserted
   *
   * @param charset the new CharacterSet
   * @throws java.io.UnsupportedEncodingException
   */
  public void insertCharacterset(CharacterSet charset) throws UnsupportedEncodingException
  {
    if (!Charset.isSupported(charset.getCharsetName()))
    {
      throw new UnsupportedEncodingException(charset.name());
    }
    this.charset = charset;
    insert(LPRCommand.ESC_37);
    insert((byte) charset.getIndex());
  }

  /**
   * Get the current characterset
   *
   * @return current characterset
   */
  public CharacterSet getCurrentCharacterset()
  {
    return charset;
  }

  /**
   * Insert point for cutting the paper. Feed the paper for the cutting point by 6 Line
   * Feeds
   */
  public void insertPaperCut()
  {
    LPRDocument.this.insert(LPRCommand.LF, LPRCommand.LF, LPRCommand.LF, LPRCommand.LF, LPRCommand.LF, LPRCommand.LF, LPRCommand.GS_V);
  }

}
