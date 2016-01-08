package com.hearthgames.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SitemapController {

    @RequestMapping(value = "/hgsitemap")
    public ModelAndView sitemap() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("sitemap");
        modelAndView.addObject("navpage", "sitemap");
        return modelAndView;
    }
}
