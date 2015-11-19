/**
 * Filename: WSConfiguration.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */

package com.sushant;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * 
 * WebSocket Configuration File.
 * @author Sushant
 *
 */

@Configuration
@EnableWebSocketMessageBroker
public class WSConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    /**
     * Configure Topic based Broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    
    /**
     * Register stomp Endpoints for receiving message
     */
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        
        /**
         * End Point to receive a message over the HTTP.
         */
        registry.addEndpoint("/receive").withSockJS();
    }
}
