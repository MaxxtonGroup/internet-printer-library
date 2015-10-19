package com.maxxton.printer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * PrintDocument is the document to be printed
 * 
 * Copyright Maxxton 2015
 *
 * @author Hermans.S
 */
public class PrintDocument
{

  private final String documentName;
  private final ByteArrayOutputStream buffer;
  private final DataOutputStream dataStream;

  private int copies = 1;

  /**
   * Create new LPR Document
   *
   * @param documentName Name of the document
   */
  public PrintDocument(String documentName)
  {
    this.buffer = new ByteArrayOutputStream();
    this.dataStream = new DataOutputStream(buffer);
    this.documentName = documentName;
  }

  /**
   * Insert Raw bytes
   *
   * @param raw raw data
   */
  public void insert(byte... raw)
  {
    try
    {
      dataStream.write(raw);
    } catch (IOException ex)
    {
    }
  }

  /**
   * Insert Raw bytes
   *
   * @param raw bytes
   * @param i   start point
   * @param l   length
   */
  public void insert(byte[] raw, int i, int l)
  {
    try
    {
      dataStream.write(raw, i, l);
    } catch (IOException ex)
    {
    }
  }

  /**
   * Insert text as string
   *
   * @param string text
   * @throws com.maxxton.printer.PrintFormatException
   */
  public void insert(String string) throws PrintFormatException
  {
    insert(string.getBytes());
  }

  public void insert(char... characters)
  {
    insert(new String(characters).getBytes());
  }

  /**
   * Insert from an inputstream
   *
   * @param in
   * @throws IOException
   * @throws com.maxxton.printer.PrintFormatException
   */
  public void insert(InputStream in) throws IOException, PrintFormatException
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
    } catch (IOException e)
    {
    }
    String string = new String(bytes.toByteArray());
    insert(string);
  }

  /**
   * Insert content of a file
   *
   * @param file
   * @throws IOException
   * @throws com.maxxton.printer.PrintFormatException
   */
  public void insert(File file) throws IOException, PrintFormatException
  {
    insert(new FileInputStream(file));
  }

  /**
   * Insert linebreak
   */
  public void insertNewLine()
  {
    insert('\n');
  }

  /**
   * Insert print layout
   *
   * @param layout PrintLayout
   * @throws com.maxxton.printer.PrintFormatException
   */
  public void insert(PrintLayout layout) throws PrintFormatException
  {
    insert(layout.toString());
  }

  /**
   * Insert sub document
   *
   * @param document sub PrintDocument
   */
  public void insert(PrintDocument document)
  {
    insert(document.getRaw());
  }

  /**
   * Insert end of the document
   */
  public void insertDocumentEnd()
  {
  }

  /**
   * Set amount of copies
   *
   * @param copies amount of copies
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
  public byte[] getRaw()
  {
    byte[] raw = buffer.toByteArray();
    byte[] copied = new byte[raw.length*getCopies()];
    for(int i = 0; i < raw.length; i++){
      for(int j = 0; j < getCopies(); j++){
        copied[raw.length*j + i] = raw[i];
      }
    }
    return copied;
  }

}
