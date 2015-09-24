package com.maxxton.printer.lpr;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * PrintDocument is the document to be printed.
 * This document is build up with LPRCommands and text.
 * These commands and text are send to the printer in a PrintJob.
 *
 * @author Hermans.S
 * @copyright Maxxton 2015
 */
public class LPRDocument
{

  private final String documentName;
  private int copies = 1;

  private CharacterSet charset = CharacterSet.NONE;

  private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private DataOutputStream dataStream = new DataOutputStream(buffer);

  /**
   * Create new LPR Document
   *
   * @param documentName
   *          Name of the document
   */
  public LPRDocument(String documentName)
  {
    this.documentName = documentName;
  }

  /**
   * Insert Raw bytes
   *
   * @param raw
   *          raw data
   */
  public void insert(byte... raw)
  {
    try
    {
      dataStream.write(raw);
    }
    catch (IOException ex)
    {
    }
  }

  /**
   * Insert Raw bytes
   *
   * @param raw
   *          bytes
   * @param i
   *          start point
   * @param l
   *          length
   */
  public void insert(byte[] raw, int i, int l)
  {
    try
    {
      dataStream.write(raw, i, l);
    }
    catch (IOException ex)
    {
    }
  }

  /**
   * Insert text as string
   *
   * @param string
   *          text
   */
  public void insert(String string)
  {
    try
    {
      dataStream.write(string.getBytes(charset.getCharset()));
    }
    catch (IOException ex)
    {
    }
  }

  /**
   * Insert LPRCommands
   *
   * @param commands
   *          LPRCommand
   */
  public void insert(LPRCommand... commands)
  {
    for (LPRCommand command : commands)
    {
      try
      {
        dataStream.write(command.getCode());
      }
      catch (IOException ex)
      {
      }
    }
  }

  /**
   * Insert from an inputstream and filter for special characters
   *
   * @param in
   * @throws IOException
   */
  public void insert(InputStream in) throws IOException
  {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    byte[] b = new byte[1024];
    int l;
    while ((l = in.read(b)) != -1)
    {
      bytes.write(b, 0, l);
    }
    try
    {
      in.close();
    }
    catch (IOException e)
    {
    }
    String string = new String(bytes.toByteArray());
    try
    {
      dataStream.write(string.getBytes(charset.getCharset()));
    }
    catch (IOException ex)
    {
    }
  }

  /**
   * Insert content of a file
   *
   * @param file
   * @throws IOException
   */
  public void insert(File file) throws IOException
  {
    LPRDocument.this.insert(new FileInputStream(file));
  }

  /**
   * Insert linebreak
   */
  public void insertLineBreak()
  {
    insert(LPRCommand.LF);
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
   * @param charset
   *          the new CharacterSet
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
   * Insert point for cutting the paper
   */
  public void insertPaperCut()
  {
    LPRDocument.this.insert(LPRCommand.LF, LPRCommand.LF, LPRCommand.LF, LPRCommand.LF, LPRCommand.LF, LPRCommand.LF, LPRCommand.GS_V);
  }

  /**
   * Set amount of copies
   *
   * @param copies
   *          amount of copies
   */
  public void setCopies(int copies)
  {
    if (copies <= 0)
    {
      throw new IllegalArgumentException("Copies must be greater than 0");
    }
    this.copies = copies;
  }

  /**
   * Get amount of copies
   *
   * @return amount of copies
   */
  public int getCopies()
  {
    return copies;
  }

  /**
   * Get document name
   *
   * @return name of the document
   */
  public String getDocumentName()
  {
    return documentName;
  }

  /**
   * Get the raw data for the printer
   *
   * @return Byte array
   */
  protected byte[] getRaw()
  {
    byte[] raw = buffer.toByteArray();
    return raw;
  }

}
