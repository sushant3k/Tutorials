/**
 * 
 */
package com.learning.qoe.model;

/**
 * @author Sushant
 *
 */
public class Status {

    private String message;

    public Status() {
        
    }
    public Status(String msg) {
        this.message = msg;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
