
package com.maxxton.printer.lpr;

import java.nio.charset.Charset;

/**
 *
 * @author hermans.s
 */
public enum CharacterSet {
    
    NONE(0, "IBM437"),
    STANDARD_EUROPE_USA(1, "IBM437");
    
    private final int index;
    private final String charset;
    
    private CharacterSet(int index, String charset){
        this.index = index;
        this.charset = charset;
    }
    
    public int getIndex(){
        return index;
    }
    
    public Charset getCharset(){
        return Charset.forName(charset);
    }
    
    public String getCharsetName(){
        return charset;
    }
    
}
