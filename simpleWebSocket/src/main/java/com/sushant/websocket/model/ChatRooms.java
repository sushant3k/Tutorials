/**
 * Filename: ChatRooms.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */
package com.sushant.websocket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sushant
 *
 */
public class ChatRooms {

    /**
     * List of ChatRooms
     */
    private List<ChatRoom> chatRoom;
    
    
    public void addChatRoom(ChatRoom cr) {
        if (chatRoom == null) {
            chatRoom = new ArrayList<ChatRooms.ChatRoom>();
        }
        chatRoom.add(cr);
    }
    
    public List<ChatRoom> getChatRoom() {
        return chatRoom;
    }



    public void setChatRooms(List<ChatRoom> chatRoom) {
        this.chatRoom = chatRoom;
    }



    /**
     * 
     * Inner class for ChatRoom
     * @author Sushant
     *
     */
    public static class ChatRoom {
        
        /**
         * ChatRoom Id
         */
        private String id;
        
        /**
         * Chatroom name
         */
        private String name;
        
        public ChatRoom() {
            
        }
        public ChatRoom(String id, String name) {
            this.id = id;
            this.name = name;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        
        
    }
}
