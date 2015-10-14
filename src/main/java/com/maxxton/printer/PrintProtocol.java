package com.maxxton.printer;

/**
 * Supported Print protocols
 *
 * @author Hermans.S
 */
public enum PrintProtocol
{

  /**
   * Raw printing
   */
  RAW(9100),
  /**
   * Line Print Remote *
   */
  LPR(515),
  /**
   * Simple version of LPR for Bota printers
   */
  SIMPLE_LPR(515);

  private final int port;

  private PrintProtocol(int port)
  {
    this.port = port;
  }

  public int getPort()
  {
    return port;
  }

}
