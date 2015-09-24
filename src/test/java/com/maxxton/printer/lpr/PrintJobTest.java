package com.maxxton.printer.lpr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for PrintJob
 * Check if print data is according to the LPR standard
 *
 * @see PrintJob
 * @see https://www.ietf.org/rfc/rfc1179.txt
 * @author Hermans.s
 * @copyright Maxxton 2015
 */
public class PrintJobTest
{

  private LPRPrinter printer;

  public PrintJobTest()
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
    printer = new LPRPrinter("localhost");
  }

  @After
  public void tearDown()
  {
  }

  /**
   * Check if the first request to the printer for the printjob is correct
   * 
   * Print Job header
   *  +----+-------+----+
   *  | 02 | Queue | LF |
   *  +----+-------+----+
   *
   * @see https://www.ietf.org/rfc/rfc1179.txt
   * @throws LPRException
   * @throws UnsupportedEncodingException
   * @throws IOException
   */
  @Test
  public void checkPrintJobHeader() throws LPRException, UnsupportedEncodingException, IOException
  {
    //Create document
    LPRDocument document = new LPRDocument("test document");

    //Create print job
    BufferedPrintJob printJob = new BufferedPrintJob(printer, document);

    //Execute print job
    printJob.print();

    //Get printed buffer
    byte[] printedBuffer = printJob.getBuffer();

    //Read command
    int command = printedBuffer[0];
    //Read queue
    ByteArrayOutputStream queue = new ByteArrayOutputStream();
    for (int i = 1; i < printedBuffer.length; i++)
    {
      if (printedBuffer[i] == LPRCommand.LF.getCode())
      {
        break;
      }
      queue.write(new byte[]
      {
        printedBuffer[i]
      });
    }

    //Check command
    assertEquals(02, command);

    assertEquals("1", new String(queue.toByteArray()));

    String output = new String(printJob.getBuffer(), "ASCII");
    System.out.println(output);
  }

  /**
   * Check if the control file and data file is sended correctly
   * 
   * Control File:
   * +----+-------+----+------+----+
   * | 02 | Count | SP | Name | LF |
   * +----+-------+----+------+----+
   * ...bytes...NULL
   * 
   * Data File:
   * +----+-------+----+------+----+
   * | 03 | Count | SP | Name | LF |
   * +----+-------+----+------+----+
   * ...bytes...NULL
   * 
   * @see https://www.ietf.org/rfc/rfc1179.txt
   * @throws com.maxxton.printer.lpr.LPRException
   * @throws java.io.IOException
   */
  @Test
  public void checkControlFileAndDataFile() throws LPRException, IOException
  {
    //Create document
    LPRDocument document = new LPRDocument("test document");
    document.insert("hello world");

    //Create print job
    BufferedPrintJob printJob = new BufferedPrintJob(printer, document);

    //Execute print job
    printJob.print();

    //Get printed buffer
    byte[] printedBuffer = printJob.getBuffer();
    System.out.println(new String(printedBuffer));
    ByteArrayInputStream bufferIn = new ByteArrayInputStream(printedBuffer);

    //Skip print job header
    int c;
    while ((c = bufferIn.read()) != -1 && c != LPRCommand.LF.getCode());
    if (c == -1)
    {
      fail(); //Fail if end of stream  is reached
    }

    //Check control file -------------------------------------------------------
    //Read command
    int cfCommand = bufferIn.read();
    //Read count
    ByteArrayOutputStream cfCountOut = new ByteArrayOutputStream();
    while ((c = bufferIn.read()) != -1 && c != LPRCommand.SPACE.getCode())
    {
      cfCountOut.write(c);
    }
    if (c == -1)
    {
      fail(); //Fail if end of stream  is reached
    }
    int cfCount = Integer.parseInt(new String(cfCountOut.toByteArray()));

    //Read name
    ByteArrayOutputStream cfNameOut = new ByteArrayOutputStream();
    while ((c = bufferIn.read()) != -1 && c != LPRCommand.LF.getCode())
    {
      cfNameOut.write(c);
    }
    if (c == -1)
    {
      fail(); //Fail if end of stream  is reached
    }
    String cfName = new String(cfNameOut.toByteArray());

    System.out.println("command: " + cfCommand);
    System.out.println("count: " + cfCount);
    System.out.println("name: " + cfName);

    //Check command
    assertEquals(02, cfCommand);

    //Check name
    assertEquals("cfA", cfName.substring(0, 3));
    int jobId = Integer.parseInt(cfName.substring(3, 6));
    assertEquals(printJob.getJobId(), jobId);
    assertEquals(printJob.getHostname(), cfName.substring(6));

    //Read control file
    byte[] controlFile = new byte[cfCount];
    c = bufferIn.read(controlFile);
    if (c == -1)
    {
      fail(); //Fail if end of stream  is reached
    }
    System.out.println("Control file: ");
    System.out.println(new String(controlFile));

    //Check ZERO byte
    assertEquals(LPRCommand.NULL.getCode(), bufferIn.read());

    //Check data file -------------------------------------------------------
    //Read command
    int dfCommand = bufferIn.read();
    //Read count
    ByteArrayOutputStream dfCountOut = new ByteArrayOutputStream();
    while ((c = bufferIn.read()) != -1 && c != LPRCommand.SPACE.getCode())
    {
      dfCountOut.write(c);
    }
    if (c == -1)
    {
      fail(); //Fail if end of stream  is reached
    }
    int dfCount = Integer.parseInt(new String(dfCountOut.toByteArray()));

    //Read name
    ByteArrayOutputStream dfNameOut = new ByteArrayOutputStream();
    while ((c = bufferIn.read()) != -1 && c != LPRCommand.LF.getCode())
    {
      dfNameOut.write(c);
    }
    if (c == -1)
    {
      fail(); //Fail if end of stream  is reached
    }
    String dfName = new String(dfNameOut.toByteArray());

    //Check command
    assertEquals(03, dfCommand);

    //Check name
    assertEquals("dfA", dfName.substring(0, 3));
    int jobId2 = Integer.parseInt(dfName.substring(3, 6));
    assertEquals(printJob.getJobId(), jobId2);
    assertEquals(printJob.getHostname(), dfName.substring(6));

    //Read control file
    byte[] dataFile = new byte[dfCount];
    c = bufferIn.read(dataFile);
    if (c == -1)
    {
      fail(); //Fail if end of stream  is reached
    }
    System.out.println("Data file: ");
    System.out.println(new String(dataFile));

    assertEquals("hello world", new String(dataFile));

    //Check ZERO byte
    assertEquals(LPRCommand.NULL.getCode(), bufferIn.read());

  }
}
