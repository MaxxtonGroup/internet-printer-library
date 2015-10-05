
package com.maxxton.printer.fgl;

/**
 *
 * @author hermans.s
 */
public enum FGLCommand
{
  
  ROTATE_TEXT_0_DEGREES("NR"),
  ROTATE_TEXT_90_DEGREES("PR"),
  ROTATE_TEXT_180_DEGREES("PU"),
  ROTATE_TEXT_270_DEGREES("RL"),
  
  ROW_COLUMN("RC"),
  
  FONT_1("F1"),
  FONT_2("F2"),
  FONT_3("F3"),
  FONT_4("F4"),
  FONT_5("F5"),
  FONT_6("F6"),
  FONT_7("F7"),
  FONT_8("F8"),
  FONT_9("F9"),
  FONT_10("F10"),
  FONT_11("F11"),
  FONT_12("F12"),
  FONT_13("F13"),
  FONT_HEIGHT_WIDTH("HW"),
  
  NEW_LINE("NR"),
  DOCUMENT_END("p"),
  BOX_SIZE("BS"),
  
  ;
  
  private String command;
  
  private FGLCommand(String command){
    this.command = command;
  }

  public String getCode()
  {
    return command;
  }
  
}
