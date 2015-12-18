package com.hearthgames.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WhereController {

    @RequestMapping(value = "/where")
    public ModelAndView where(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("where");

        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            if (userAgent.toLowerCase().contains("windows")) {
                modelAndView.addObject("windows", true);
                modelAndView.addObject("mac", false);
                modelAndView.addObject("android", false);
            } else if (userAgent.toLowerCase().contains("mac")) {
                modelAndView.addObject("mac", true);
                modelAndView.addObject("windows", false);
                modelAndView.addObject("android", false);
            } else if (userAgent.toLowerCase().contains("android")) {
                modelAndView.addObject("android", true);
                modelAndView.addObject("windows", false);
                modelAndView.addObject("mac", false);
            }
        } else {
            modelAndView.addObject("windows", true);
            modelAndView.addObject("mac", false);
            modelAndView.addObject("android", false);
        }

        return modelAndView;
    }

}
