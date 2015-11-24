package com.hearthlogs.server.controller;

import com.hearthlogs.server.config.security.UserInfo;
import com.hearthlogs.server.database.domain.GamePlayed;
import com.hearthlogs.server.database.service.GamePlayedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@PreAuthorize("hasRole('USER')")
public class GamesController {

    @Autowired
    private GamePlayedService gamePlayedService;

    @RequestMapping(value = "/games")
    public ModelAndView listGames() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("games");

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String gameAccountId = gamePlayedService.getGameAccountId(userInfo.getBattletag());
        List<GamePlayed> gamesPlayed = gamePlayedService.getGamesPlayed(gameAccountId);

        modelAndView.addObject("gamesPlayed", gamesPlayed);
        return modelAndView;
    }
}