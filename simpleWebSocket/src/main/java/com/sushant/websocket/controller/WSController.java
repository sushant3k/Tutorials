/**
 * Filename: WSController.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */
package com.sushant.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sushant.websocket.model.ApplicationMessage;
import com.sushant.websocket.service.WSService;



/**
 * @author Sushant
 *
 */

@Controller
@RequestMapping("/")
public class WSController {

    /**
     * Inject WSService
     */
    @Autowired
    private WSService wsService;
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getApp() {
        return new ModelAndView("app");
    }
    
    @RequestMapping(value = "home", method = RequestMethod.GET)
    public ModelAndView getHome() {
        return new ModelAndView("home");
    }
    
    
    
    @MessageMapping("/receive")
    public void receiveMessage(WSMessage msg) {
        
        if (null == msg || null == msg.getChannelId() || msg.getChannelId().isEmpty()) {
            return;
        }
        
        ApplicationMessage am = new ApplicationMessage(msg.getContent());
        wsService.sendMessage(msg.getChannelId(), am);
    }
    
}
