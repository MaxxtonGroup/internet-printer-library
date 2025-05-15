package com.maxxton.printer;

import com.maxxton.printer.lpr.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit tests for LPRDocument
 *
 * Copyright Maxxton 2015
 *
 * @author Hermans.S
 * @see LPRDocument
 */
public class PrintDocumentTest {

  public PrintDocumentTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test if bytes are inserted correctly
   */
  @Test
  public void insertBytes() {
    //Create new document
    LPRDocument doc = new LPRDocument("test doc");

    //Insert bytes in the document
    doc.insert((byte) 0, (byte) 50, (byte) 150);

    //Check if the raw output of the document is the same
    byte[] raw = doc.getRaw();
    assertEquals(raw.length, 3);
    assertEquals(raw[0], (byte) 0);
    assertEquals(raw[1], (byte) 50);
    assertEquals(raw[2], (byte) 150);
  }

  /**
   * Test if bytes are inserted correctly, with start position and length
   */
  @Test
  public void insertBytesPosition() {
    //Create new document
    PrintDocument doc = new PrintDocument("test doc");

    //Insert bytes in the document
    byte[] data = new byte[] { (byte) 0, (byte) 50, (byte) 150 };
    doc.insert(data, 1, 1);

    //Check if the raw output of the document is the same
    byte[] raw = doc.getRaw();
    assertEquals(raw.length, 1);
    assertEquals(raw[0], (byte) 50);
  }

  /**
   * Test if bytes are inserted correctly
   *
   * @throws java.io.IOException
   */
  @Test
  public void insertFromInputstream() throws IOException, PrintFormatException {
    //Create new document
    PrintDocument doc = new PrintDocument("test doc");

    //Insert string in the document for an InputStream
    String input = "Hello world";
    ByteArrayInputStream byteIn = new ByteArrayInputStream(input.getBytes("UTF-8"));
    doc.insert(byteIn);

    //Check if the raw output of the document is the same
    byte[] raw = doc.getRaw();
    String output = new String(raw, "UTF-8");
    assertEquals(input, output);
  }

  /**
   * Test if linefeed is inserted correctly
   */
  @Test
  public void insertLineFeed() {
    //Create new document
    PrintDocument doc = new PrintDocument("test doc");

    //Insert line feed
    doc.insertNewLine();

    byte[] raw = doc.getRaw();
    assertEquals(raw.length, 1);
    assertEquals(raw[0], LPRCommand.LF.getCodes()[0]);
  }

}
