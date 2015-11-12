package com.maxxton.printer.fgl;

import com.maxxton.printer.PrintFormatException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the FGL document
 *
 * Copyright Maxxton 2015
 *
 * @author hermans.s
 */
public class FGLDocumentTest
{

  public FGLDocumentTest()
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
   * Test the max row length of 31
   */
  @Test
  public void testMaxRowLength(){
    //Test 30
    try{
      FGLDocument doc = new FGLDocument("");
      doc.insert("123456789012345678901234567890");
      assertTrue(true);
    }catch(PrintFormatException e){
      fail("30");
    }
    //Test 31
    try{
      FGLDocument doc = new FGLDocument("");
      doc.insert("1234567890123456789012345678901");
      assertTrue(true);
    }catch(PrintFormatException e){
      fail("31");
    }
    //Test 32
    try{
      FGLDocument doc = new FGLDocument("");
      doc.insert("12345678901234567890123456789012");
      fail("32");
    }catch(PrintFormatException e){
      assertTrue(true);
    }
  }

}
