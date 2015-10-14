package com.maxxton.printer.lpr;

/**
 * ASCII characters to create LPRCommands
 * https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=72
 *
 * @author Hermans.S Copyright Maxxton 2015
 *
 */
public enum LPRCommand
{

  NULL(0), SOH(1), STX(2), ETX(3), /**
   * Horizontal tab
   */
  HT(9), /**
   * Line feed
   */
  LF(10), /**
   * Paper cut
   */
  GS_V(29, 86),
  /**
   * Set Character set
   */
  ESC_37(27, 37), /**
   * Space
   */
  SPACE(127);

  private byte[] ascii;

  private LPRCommand(int... ascii)
  {
    byte[] bytes = new byte[ascii.length];
    for (int i = 0; i < ascii.length; i++)
    {
      bytes[i] = (byte) ascii[i];
    }
    this.ascii = bytes;
  }

  private LPRCommand(byte... ascii)
  {
    this.ascii = ascii;
  }

  /**
   * Get ASCII bytes
   *
   * @return ASCII bytes
   */
  public byte[] getCodes()
  {
    return ascii;
  }

  /**
   * Get first ASCII byte
   *
   * @return first ASCII byte
   */
  public byte getCode()
  {
    return ascii[0];
  }

}
