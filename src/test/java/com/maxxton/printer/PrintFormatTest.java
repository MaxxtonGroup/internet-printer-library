package com.maxxton.printer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hermans.s
 */
public class PrintFormatTest
{

  public PrintFormatTest()
  {
  }

  @BeforeClass
  public static void setUpClass()
  {
  }

  @AfterClass
  public static void tearDownClass()
  {
  }

  @Before
  public void setUp()
  {
  }

  @After
  public void tearDown()
  {
  }

  /**
   * Test if the formatter works
   */
  @Test
  public void testFormat()
  {
    PrintFormat format = new PrintFormat("hello %s", "world");
    assertEquals("hello world", format.toString());
  }

  /**
   * Test if you can insert an InputStream
   *
   * @throws IOException
   */
  @Test
  public void testInputStream() throws IOException
  {
    ByteArrayInputStream buffer = new ByteArrayInputStream("hello %s".getBytes());
    PrintFormat format = new PrintFormat(buffer, "world");
    assertEquals("hello world", format.toString());
  }
}
