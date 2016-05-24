/**
 * 
 */
package com.learning.qoe.exception;

/**
 * @author ggne0084
 *
 */
public class QoEException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public QoEException() {
        super();
    }
    
    public QoEException(Throwable t) {
        super(t);
    }
    
    public QoEException(String message) {
        super(message);
    }
    
    public QoEException(String message, Throwable e) {
        super(message, e);
    }
}
