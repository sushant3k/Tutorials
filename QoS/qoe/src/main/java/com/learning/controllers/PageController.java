package com.learning.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class PageController {

    
    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public ModelAndView getIndexPage()
    {
        ModelAndView mv = new ModelAndView("dashboard");                
        return mv;
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home()
    {
        ModelAndView mv = new ModelAndView("app");                
        return mv;
    }
    
    @RequestMapping(value="/details", method = RequestMethod.GET)
    public ModelAndView detailsPage() {
        
        return new ModelAndView("details");
    }
    @MessageMapping("/receive")
    @SendTo("/topic/message")
    public HelloMessage receiveMessage(HelloMessage msg) {
        HelloMessage hm = new HelloMessage();
        
        hm.setName("Hi : " + msg);
        return hm;
    }
}
