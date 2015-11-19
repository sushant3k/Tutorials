/**
 * 
 */
package com.sushant.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.sushant.websocket.model.ApplicationMessage;

/**
 * @author Sushant
 *
 */
@Service
public class WSServiceImpl implements WSService{

    /**
     * A Template for sending messages to the subscribed Clients.
     */
    @Autowired
    private SimpMessagingTemplate template;
    
    @Override
    public void sendMessage(String channelId, ApplicationMessage message) {
        
        template.convertAndSend("/topic/"+channelId, message);
    }

}
