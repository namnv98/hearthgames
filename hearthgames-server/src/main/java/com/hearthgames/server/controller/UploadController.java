package com.hearthgames.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UploadController {

    @RequestMapping(value = "/upload")
    public ModelAndView upload() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upload");
        modelAndView.addObject("navpage", "upload");
        return modelAndView;
    }
}