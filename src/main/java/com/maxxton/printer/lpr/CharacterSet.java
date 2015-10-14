package com.maxxton.printer.lpr;

import java.nio.charset.Charset;

/**
 * CharacterSet for printing
 * https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=71
 *
 * @author hermans.s Copyright Maxxton 2015
 *
 * @see java.nio.charset.Charset
 */
public enum CharacterSet
{

  NONE(0, "IBM437"), STANDARD_EUROPE_USA(1, "IBM437");

  private final int index;
  private final String charset;

  /**
   * Define characterset for the printer
   *
   * @param index charset index on the printer
   * @param charset charset name
   */
  private CharacterSet(int index, String charset)
  {
    this.index = index;
    this.charset = charset;
  }

  public int getIndex()
  {
    return index;
  }

  public Charset getCharset()
  {
    return Charset.forName(charset);
  }

  public String getCharsetName()
  {
    return charset;
  }

}
