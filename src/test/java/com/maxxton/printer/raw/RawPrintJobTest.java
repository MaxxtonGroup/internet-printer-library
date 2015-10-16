package com.maxxton.printer.raw;

import com.maxxton.printer.PrintDocument;
import com.maxxton.printer.PrintException;
import com.maxxton.printer.Printer;
import com.maxxton.printer.lpr.BufferedLPRPrintJob;
import com.maxxton.printer.lpr.LPRDocument;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test raw printjob
 *
 * Copyright Maxxton 2015
 *
 * @author hermans.s
 */
public class RawPrintJobTest
{
  private Printer printer;

  public RawPrintJobTest()
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

  @Test
  public void testPrintJob() throws PrintException
  {
    //Create document
    PrintDocument document = new PrintDocument("test document");
    document.insert("test");

    //Create print job
    BufferedRawPrintJob printJob = new BufferedRawPrintJob(printer, document);

    //Execute print job
    printJob.print();

    //Get printed buffer
    byte[] printedBuffer = printJob.getBuffer();
    assertEquals("test", new String(printedBuffer));
  }

}
