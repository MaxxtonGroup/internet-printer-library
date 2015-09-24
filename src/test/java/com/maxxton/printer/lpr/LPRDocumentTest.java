package com.maxxton.printer.lpr;

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
 * @see LPRDocument
 *
 * @author Hermans.S
 * @copyright Maxxton 2015
 */
public class LPRDocumentTest
{

  public LPRDocumentTest()
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
   * Test if bytes are inserted correctly
   */
  @Test
  public void insertBytes()
  {
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
  public void insertBytesPosition()
  {
    //Create new document
    LPRDocument doc = new LPRDocument("test doc");
    
    //Insert bytes in the document
    byte[] data = new byte[]{(byte) 0, (byte) 50, (byte) 150};
    doc.insert(data, 1, 1);
    
    //Check if the raw output of the document is the same
    byte[] raw = doc.getRaw();
    assertEquals(raw.length, 1);
    assertEquals(raw[0], (byte) 50);
  }

  /**
   * Test if bytes are inserted correctly
   * @throws java.io.IOException
   */
  @Test
  public void insertFromInputstream() throws IOException
  {
    //Create new document
    LPRDocument doc = new LPRDocument("test doc");
    
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
  public void insertLineFeed(){
    //Create new document
    LPRDocument doc = new LPRDocument("test doc");
    
    //Insert line feed
    doc.insertLineFeed();
    
    byte[] raw = doc.getRaw();
    assertEquals(raw.length, 1);
    assertEquals(raw[0], LPRCommand.LF.getCodes()[0]);
  }
  
  /**
   * Test if tab is inserted correctly
   */
  @Test
  public void insertTab(){
    //Create new document
    LPRDocument doc = new LPRDocument("test doc");
    
    //Insert line feed
    doc.insertTab();
    
    byte[] raw = doc.getRaw();
    assertEquals(raw.length, 1);
    assertEquals(raw[0], LPRCommand.HT.getCodes()[0]);
  }
  
  /**
   * Test if papercut is inserted correctly
   */
  @Test
  public void insertPaperCut(){
    //Create new document
    LPRDocument doc = new LPRDocument("test doc");
    
    //Insert line feed
    doc.insertPaperCut();
    
    //Check result
    byte[] check = new byte[]{
      LPRCommand.LF.getCodes()[0],
      LPRCommand.LF.getCodes()[0],
      LPRCommand.LF.getCodes()[0],
      LPRCommand.LF.getCodes()[0],
      LPRCommand.LF.getCodes()[0],
      LPRCommand.LF.getCodes()[0],
      LPRCommand.GS_V.getCodes()[0],
      LPRCommand.GS_V.getCodes()[1]
    };
    byte[] raw = doc.getRaw();
    assertArrayEquals(check, raw);
  }
  
  /**
   * Check if default characterset is ASCII
   * @throws java.io.UnsupportedEncodingException
   */
  @Test
  public void testDefaultCharacterSet() throws UnsupportedEncodingException{
    //Create new document
    LPRDocument doc = new LPRDocument("test doc");
    
    //Generate example data
    byte[] asciiBytes = new byte[256];
    for(int i = 0; i <= 255; i++){
      asciiBytes[i] = (byte)i;
    }
    String asciiString = new String(asciiBytes, "ASCII");
    
    //Insert generated data
    doc.insert(asciiString);
    
    //Check result
    byte[] raw = doc.getRaw();
    
    //Only the first 128 bytes should be the same
    for(int i = 0; i < 128; i++){
      assertEquals(asciiBytes[i], raw[i]);
    }
    
    //Last 128 bytes should be differend
    for(int i = 128; i < 256; i++){
      assertEquals(63, raw[i]);
    }
  }
  
  /**
   * Test if STANDARD_EUROPE_USA is encoded with IBM437
   * @throws java.io.UnsupportedEncodingException
   */
  @Test
  public void testOtherCharacterSet() throws UnsupportedEncodingException{
    //Create new document
    LPRDocument doc = new LPRDocument("test doc");
    
    //Change charset to STANDARD_EUROPA_USA
    doc.insertCharacterset(CharacterSet.STANDARD_EUROPE_USA);
    
    //Generate example data
    byte[] ibm437Bytes = new byte[256];
    for(int i = 0; i <= 255; i++){
      ibm437Bytes[i] = (byte)i;
    }
    String asciiString = new String(ibm437Bytes, "IBM437");
    
    //Insert generated data
    doc.insert(asciiString);
    
    //Check result
    byte[] raw = doc.getRaw();
    
    //Get first 3 bytes for changing the charset
    byte[] charsetCommand = Arrays.copyOfRange(raw, 0, 3);
    byte[] checkCommand = new byte[]{
            LPRCommand.ESC_37.getCodes()[0],
            LPRCommand.ESC_37.getCodes()[1], (byte)1};
    assertArrayEquals(checkCommand, charsetCommand);
    
    //Only the first 128 bytes should be the same
    for(int i = 0; i < 256; i++){
      assertEquals(ibm437Bytes[i], raw[i+3]);
    }
  }
  
}
