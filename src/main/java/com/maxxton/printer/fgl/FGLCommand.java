package com.maxxton.printer.fgl;

/**
 * FGL commands
 * http://portal.siriusware.com/docs/index.html#page/Version%25204.3%2520Salesware%2520Documentation%2FMASTER_43_Layouts.17.40.html
 * Copyright Maxxton 2015
 *
 * @author Hermans.S
 */
public enum FGLCommand
{

  TEXT_ORIENTATION_PORTRET("NR"),
  TEXT_ORIENTATION_LANDSCAPE_INVERTED("PR"),
  TEXT_ORIENTATION_PORTRET_INVERTED("PU"),
  TEXT_ORIENTATION_LANDSCAPE("RL"),
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
  BOX_SIZE("BS"),;

  private final String command;

  private FGLCommand(String command)
  {
    this.command = command;
  }

  public String getCode()
  {
    return command;
  }

}
