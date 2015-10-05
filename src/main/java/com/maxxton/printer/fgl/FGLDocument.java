
package com.maxxton.printer.fgl;

import com.maxxton.printer.PrintDocument;

/**
 *
 * @author hermans.s
 */
public class FGLDocument extends PrintDocument
{

  public FGLDocument(String documentName)
  {
    super(documentName);
  }
  
  public void insert(FGLCommand command, String... args){
    insert("<" + command.getCode());
    for(int i = 0; i < args.length; i++){
      if(i > 0){
        insert(",");
      }
      insert(args[i]);
    }
    insert(">");
  }
  
  public void insertRowAndColumn(int row, int column){
    insert(FGLCommand.ROW_COLUMN, String.valueOf(row), String.valueOf(column));
  }
  
  public void insertFontHeightWidht(int height, int width){
    insert(FGLCommand.FONT_HEIGHT_WIDTH, String.valueOf(height), String.valueOf(width));
  }
  
  public void insertFont(int font){
    FGLCommand fontCommand;
    switch(font){
      case 1:
        fontCommand = FGLCommand.FONT_1;
        break;
      case 2:
        fontCommand = FGLCommand.FONT_2;
        break;
      case 3:
        fontCommand = FGLCommand.FONT_3;
        break;
      case 4:
        fontCommand = FGLCommand.FONT_4;
        break;
      case 5:
        fontCommand = FGLCommand.FONT_5;
        break;
      case 6:
        fontCommand = FGLCommand.FONT_6;
        break;
      case 7:
        fontCommand = FGLCommand.FONT_7;
        break;
      case 8:
        fontCommand = FGLCommand.FONT_8;
        break;
      case 9:
        fontCommand = FGLCommand.FONT_9;
        break;
      case 10:
        fontCommand = FGLCommand.FONT_10;
        break;
      case 11:
        fontCommand = FGLCommand.FONT_11;
        break;
      case 12:
        fontCommand = FGLCommand.FONT_12;
        break;
      case 13:
        fontCommand = FGLCommand.FONT_13;
        break;
      default:
        throw new IllegalStateException("Font '" + font + "' not supported");
    }
    insert(fontCommand);
  }
  
  public void insertBoxSize(int width, int height){
    insert(FGLCommand.BOX_SIZE, String.valueOf(width), String.valueOf(height));
  }

  @Override
  public void insertLineFeed()
  {
    insert(FGLCommand.NEW_LINE);
  }
  
  public void end(){
    insert(FGLCommand.DOCUMENT_END);
  }
  
}
