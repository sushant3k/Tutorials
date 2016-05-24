
package com.learning.qoe.model;

import java.io.Serializable;

/**
 * @author Sushant Jain
 *
 */
public class HTTPParams implements Serializable{

    /**
     * 
     */
    private String filename;
    
    private String payload; 
    
    private String header;
    
    /**
     * Identifies whether it is a HTTP request or response.
     */
    private boolean request = true; // defaulting to true for backward compatibility
    
    public String getHeader() {
        return header;
    }

    
    public boolean isRequest() {
        return request;
    }


    public void setRequest(boolean request) {
        this.request = request;
    }


    public void setHeader(String header) {
        this.header = header;
    }

    public HTTPParams() {
        
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    
}
