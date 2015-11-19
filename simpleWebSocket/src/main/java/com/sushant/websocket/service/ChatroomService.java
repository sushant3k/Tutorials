/**
 * Filename: ChatroomService.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */
package com.sushant.websocket.service;

import com.sushant.websocket.model.ChatRooms;

/**
 * @author Sushant
 *
 */
public interface ChatroomService {

    /**
     * Get All ChatRooms
     * @return {@link ChatRooms}
     */
    ChatRooms getAllChatRooms();
}
