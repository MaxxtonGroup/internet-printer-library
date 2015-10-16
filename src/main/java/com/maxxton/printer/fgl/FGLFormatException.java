
package com.maxxton.printer.fgl;

/**
 * Exception when FGL format is incorrect
 * 
 * Copyright Maxxton 2015
 * @author hermans.s
 */
public class FGLFormatException extends RuntimeException {

    /**
     * Creates a new instance of <code>FGLException</code> without detail message.
     */
    public FGLFormatException() {
    }


    /**
     * Constructs an instance of <code>FGLException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FGLFormatException(String msg) {
        super(msg);
    }
}
