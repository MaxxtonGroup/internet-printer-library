
package com.maxxton.printer;

import com.maxxton.printer.fgl.FGLDocument;

/**
 *
 * @author hermans.s
 */
public enum PrintFormats
{
  
  PLAIN(PrintDocument.class), 
  FGL(FGLDocument.class);
  
  private final Class documentClass;
  
  private PrintFormats(Class<? extends PrintDocument> documentClass){
    this.documentClass = documentClass;
  }
  
  public PrintDocument getDocument(){
    return getDocument("");
  }
  
  public PrintDocument getDocument(String title){
    try{
      return (PrintDocument) documentClass.getConstructor(String.class).newInstance(title);
    }catch(Exception e){
      return null;
    }
  }
  
}
