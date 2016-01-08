package com.hearthgames.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DownloadClientController {

    @RequestMapping(value = "/download")
    public ModelAndView downloadClient() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("download");
        modelAndView.addObject("navpage", "download");
        return modelAndView;
    }

}
