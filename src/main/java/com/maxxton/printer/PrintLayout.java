package com.maxxton.printer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Specify print format
 *
 * @author hermans.s
 */
public class PrintLayout
{

  private final String format;
  private final Map<Integer, Object> args;

  /**
   * Create new format
   *
   * @param file File which contains a format string
   * @param args Arguments referenced by the format specifiers in the format string. If
   * there are more arguments than format specifiers, the extra arguments are ignored. The
   * number of arguments is variable and may be zero. The maximum number of arguments is
   * limited by the maximum dimension of a Java array as defined by
   * <cite>The Java&trade; Virtual Machine Specification</cite>. The behaviour on a
   * {@code null} argument depends on the conversion.
   * @throws java.io.IOException
   */
  public PrintLayout(File file, Object... args) throws IOException
  {
    this(new FileInputStream(file), args);
  }

  /**
   * Create new format
   *
   * @param in Inputstream to read a format string
   * @param args Arguments referenced by the format specifiers in the format string. If
   * there are more arguments than format specifiers, the extra arguments are ignored. The
   * number of arguments is variable and may be zero. The maximum number of arguments is
   * limited by the maximum dimension of a Java array as defined by
   * <cite>The Java&trade; Virtual Machine Specification</cite>. The behaviour on a
   * {@code null} argument depends on the conversion.
   * @throws java.io.IOException
   */
  public PrintLayout(InputStream in, Object... args) throws IOException
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
    format = new String(bytes.toByteArray());

    this.args = new HashMap();
    for (int i = 0; i < args.length; i++)
    {
      this.args.put(i, args[i]);
    }
  }

  /**
   * Create new format
   *
   * @param format A format string
   * @param args Arguments referenced by the format specifiers in the format string. If
   * there are more arguments than format specifiers, the extra arguments are ignored. The
   * number of arguments is variable and may be zero. The maximum number of arguments is
   * limited by the maximum dimension of a Java array as defined by
   * <cite>The Java&trade; Virtual Machine Specification</cite>. The behaviour on a
   * {@code null} argument depends on the conversion.
   */
  public PrintLayout(String format, Object... args)
  {
    this.format = format;

    this.args = new HashMap();
    for (int i = 0; i < args.length; i++)
    {
      this.args.put(i, args[i]);
    }
  }

  /**
   * Set an argument for a specific index
   *
   * @param index The index of the argument
   * @param arg The argument
   */
  public void setArgument(int index, Object arg)
  {
    this.args.put(index, arg);
  }

  /**
   * Return formatted String
   *
   * @return formatted String
   */
  @Override
  public String toString()
  {
    int max = 0;
    for (Integer i : args.keySet())
    {
      if (i > max)
      {
        max = i;
      }
    }
    Object[] a = new Object[max + 1];
    for (int i = 0; i < max + 1; i++)
    {
      a[i] = args.get(i);
    }
    return String.format(format, a);
  }

}
