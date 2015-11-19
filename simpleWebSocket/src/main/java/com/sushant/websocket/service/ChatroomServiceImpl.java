/**
 * Filename: ChatroomServiceImpl.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */
package com.sushant.websocket.service;

import org.springframework.stereotype.Service;

import com.sushant.websocket.model.ChatRooms;
import com.sushant.websocket.model.ChatRooms.ChatRoom;

/**
 * @author Sushant
 *
 */
@Service
public class ChatroomServiceImpl implements ChatroomService{

    @Override
    public ChatRooms getAllChatRooms() {
        int id = 1;
        ChatRooms cr = new ChatRooms();
        cr.addChatRoom(new ChatRoom("channel"+(id++),"Personal"));
        cr.addChatRoom(new ChatRoom("channel"+(id++),"Business"));
        cr.addChatRoom(new ChatRoom("channel"+(id++),"Education"));
        cr.addChatRoom(new ChatRoom("channel"+(id++),"Miscellaneous"));        
        return cr;
    }

    
}
