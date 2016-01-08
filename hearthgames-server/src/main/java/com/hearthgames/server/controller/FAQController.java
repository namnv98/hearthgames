package com.hearthgames.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FAQController {

    @RequestMapping(value = "/faq")
    public ModelAndView faq() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("faq");
        modelAndView.addObject("navpage", "faq");
        return modelAndView;
    }

}
