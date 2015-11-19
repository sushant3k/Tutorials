/**
 * Filename: ApplicationMessage.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */
package com.sushant.websocket.model;

import java.io.Serializable;

/**
 * @author Sushant
 *
 */
public class ApplicationMessage implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * This variable contains message that is to be sent to the subscribed Clients.
     */
    private String message;
    
    public ApplicationMessage() {
        
    }

    public ApplicationMessage(String msg) {
        this.message = msg;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
