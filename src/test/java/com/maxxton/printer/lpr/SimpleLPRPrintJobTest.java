package com.maxxton.printer.lpr;

import com.maxxton.printer.PrintException;
import com.maxxton.printer.Printer;
import com.maxxton.printer.lpr.LPRCommand;
import com.maxxton.printer.lpr.LPRDocument;
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
 * JUnit tests for SimpleLPRPrintJob.
 * 
 * Copyright Maxxton 2015
 *
 * @see PrintJob
 * @see https://www.ietf.org/rfc/rfc1179.txt
 *
 * @author Hermans.s
 */
public class SimpleLPRPrintJobTest
{

  private Printer printer;

  public SimpleLPRPrintJobTest()
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
    printer = new Printer("localhost");
  }

  @After
  public void tearDown()
  {
  }

  /**
   * Check if the first request to the printer for the printjob is correct
   *
   * Print Job header +----+-------+----+ | 02 | Queue | LF |
   * +----+-------+----+
   *
   * @see https://www.ietf.org/rfc/rfc1179.txt
   * @throws PrintException
   * @throws UnsupportedEncodingException
   * @throws IOException
   */
  @Test
  public void checkPrintJobHeader() throws PrintException, UnsupportedEncodingException, IOException
  {
    //Create document
    SimpleLPRDocument document = new SimpleLPRDocument("test document");

    //Create print job
    BufferedSimpleLPRPrintJob printJob = new BufferedSimpleLPRPrintJob(printer, document);

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
   * Control File: +----+-------+----+------+----+ | 02 | Count | SP | Name | LF
   * |
   * +----+-------+----+------+----+ ...bytes...NULL
   *
   * Data File: +----+-------+----+------+----+ | 03 | Count | SP | Name | LF |
   * +----+-------+----+------+----+ ...bytes...NULL
   *
   * @see https://www.ietf.org/rfc/rfc1179.txt
   * @throws com.maxxton.printer.PrintException
   * @throws java.io.IOException
   */
  @Test
  public void checkDataFile() throws PrintException, IOException
  {
    //Create document
    String documentText = "hello world";
    SimpleLPRDocument document = new SimpleLPRDocument("test document");
    document.insert(documentText);

    //Create print job
    BufferedSimpleLPRPrintJob printJob = new BufferedSimpleLPRPrintJob(printer, document);

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

    assertEquals(documentText, new String(dataFile));

  }
}
