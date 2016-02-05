package com.hearthgames.server.controller;

import com.hearthgames.server.database.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");

        modelAndView.addObject("players", gameService.getPlayers());

        return modelAndView;
    }
}
