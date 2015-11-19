/**
 * Filename: WSRestController.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */
package com.sushant.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sushant.websocket.model.ChatRooms;
import com.sushant.websocket.service.ChatroomService;

/**
 * @author Sushant
 *
 */
@RestController
public class WSRestController {

    /**
     * Inject ChatroomService
     */
    @Autowired
    private ChatroomService crService;
    
    /**
     * 
     * @return
     */
    @RequestMapping(value="/chatrooms", 
            method = { RequestMethod.GET },             
            produces = {"application/json"})
    public ChatRooms getAll() {
        return crService.getAllChatRooms();
    }
}
