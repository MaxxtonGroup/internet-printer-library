
package com.maxxton.printer.lpr;

/**
 * Simple LPR document with no document end
 * 
 * Copyright Maxxton 2015
 * @author hermans.s
 */
public class SimpleLPRDocument extends LPRDocument{

  public SimpleLPRDocument(String documentName)
  {
    super(documentName);
  }

  @Override
  public void insertDocumentEnd()
  {
    //do nothing
  }

}
