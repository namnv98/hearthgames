package com.hearthgames.utils.hearthpwn.controller;

import com.hearthgames.utils.hearthpwn.CardLink;
import com.hearthgames.utils.hearthpwn.CardParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CardLinkUpdateController {

    @Autowired
    private CardParser cardParser;

    @RequestMapping("/link")
    public ModelAndView link() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("links");

        List<CardLink> cardLinks = cardParser.getUnknownCardLinks();
        modelAndView.addObject("cardLinks", cardLinks);

        return modelAndView;
    }

}
