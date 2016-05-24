
package com.learning.qoe.model;

import java.io.Serializable;

/**
 * @author Sushant
 *
 */
public class HLSParams extends Params implements Serializable{

    /**
     * 
     */
    
    private String filename;
    
    public HLSParams() {
        
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    
}
