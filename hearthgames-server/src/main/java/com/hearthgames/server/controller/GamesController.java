package com.hearthgames.server.controller;

import com.hearthgames.server.config.security.UserInfo;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class GamesController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/account/{gameAccountId}/games")
    public ModelAndView listGames(@PathVariable String gameAccountId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("games");

        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(gameAccountId);

        modelAndView.addObject("gamesPlayed", gamesPlayed);
        return modelAndView;
    }

    @RequestMapping(value = "/mygames")
    public ModelAndView listGames() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("games");

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String gameAccountId = gameService.getGameAccountId(userInfo.getBattletag());
        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(gameAccountId);

        modelAndView.addObject("gamesPlayed", gamesPlayed);
        return modelAndView;
    }

    @RequestMapping(value = "/casualgames")
    public ModelAndView listCasualGames() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("games");

        Iterable<GamePlayed> gamesPlayed = gameService.getCasualGamesPlayed();

        modelAndView.addObject("gamesPlayed", gamesPlayed);
        return modelAndView;
    }

    @RequestMapping(value = "/rankedgames")
    public ModelAndView listRankedGames() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("games");

        Iterable<GamePlayed> gamesPlayed = gameService.getRankedGamesPlayed();

        modelAndView.addObject("gamesPlayed", gamesPlayed);
        return modelAndView;
    }

}