/**
 * 
 */
package com.learning.qoe.exception;

/**
 * @author ggne0084
 *
 */
public class QoEHttpException extends QoEException{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public QoEHttpException() {
        super();
    }
    
    public QoEHttpException(Throwable t) {
        super(t);
    }
    
    public QoEHttpException(String message) {
        super(message);
    }
    
    public QoEHttpException(String message, Throwable e) {
        super(message, e);
    }
}
