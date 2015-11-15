package com.hearthlogs.server.controller;

import com.hearthlogs.server.util.HearthPwnCardLink;
import com.hearthlogs.server.util.HearthPwnCardParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HearthpwnCardLinkUpdateController {

    @Autowired
    private HearthPwnCardParser cardParser;

    @RequestMapping("/link")
    public ModelAndView link() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("links");

        List<HearthPwnCardLink> cardLinks = cardParser.getUnknownCardLinks();
        modelAndView.addObject("cardLinks", cardLinks);

        return modelAndView;
    }

}
