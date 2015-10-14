package com.maxxton.printer;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Connection with the printer
 *
 * @author Hermans.S Copyright Maxxton 2015
 */
public class PrinterConnection implements Closeable
{

  private final InputStream inputStream;
  private final OutputStream outputStream;
  private final Socket socket;

  /**
   * Create PrinterConnection based on socket connection
   *
   * @param socket Socket connection
   * @throws IOException
   */
  public PrinterConnection(Socket socket) throws IOException
  {
    this.socket = socket;
    this.inputStream = socket.getInputStream();
    this.outputStream = socket.getOutputStream();
  }

  /**
   * Create PrinterConnection based on InputStream and OutputStream
   *
   * @param in InputStream
   * @param out OutputStream
   */
  public PrinterConnection(InputStream in, OutputStream out)
  {
    socket = null;
    this.inputStream = in;
    this.outputStream = out;
  }

  public InputStream getInputStream()
  {
    return inputStream;
  }

  public OutputStream getOutputStream()
  {
    return outputStream;
  }

  public Socket getSocket()
  {
    return socket;
  }

  @Override
  public void close() throws IOException
  {
    if (inputStream != null)
    {
      inputStream.close();
    }
    if (outputStream != null)
    {
      outputStream.close();
    }
    if (socket != null)
    {
      socket.close();
    }
  }

}
