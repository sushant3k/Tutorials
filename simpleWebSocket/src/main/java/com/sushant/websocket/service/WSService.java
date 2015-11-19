/**
 * Filename: WSService.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */
package com.sushant.websocket.service;

import com.sushant.websocket.model.ApplicationMessage;

/**
 * Interface for sending messages to the subscribed Clients.
 * @author Sushant
 *
 */
public interface WSService {

    /**
     * This method sends messages to the clients with specific clientID. 
     * @param channelId - A specific destination to which message should be sent.
     * @param message - Message content.
     */
    void sendMessage(final String channelId, final ApplicationMessage message);
}
