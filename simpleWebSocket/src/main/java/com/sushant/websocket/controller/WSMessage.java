/**
 * Filename: WSMessage.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */
package com.sushant.websocket.controller;

import java.io.Serializable;

/**
 * @author Sushant
 *
 */
public class WSMessage implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -52555739796026445L;
    
    private String content;
    private String channelId;
    
    public WSMessage() {
        
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    
    
    
}
