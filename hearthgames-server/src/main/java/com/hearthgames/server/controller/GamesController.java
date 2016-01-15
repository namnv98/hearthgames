package com.hearthgames.server.controller;

import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.log.domain.GameType;
import com.hearthgames.server.util.GamesPlayedWrapper;
import com.hearthgames.server.util.GamesPlayedWrapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
        Long count = gameService.getGamesPlayedCount(gameAccountId);
        int pages = (int) Math.ceil((double) count / 10);

        GamesPlayedWrapperUtil.addGamesPlayed(modelAndView, gamesPlayed, true, pages);
        return modelAndView;
    }

    @RequestMapping(value = "/games/{gameType}")
    public ModelAndView listGames(@PathVariable int gameType) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("games");

        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(new PageRequest(0, 10), gameType);
        Long count = gameService.getGamesPlayedCount(gameType);
        int pages = (int) Math.ceil((double) count / 10);

        GamesPlayedWrapperUtil.addGamesPlayed(modelAndView, gamesPlayed, GameType.getGameType(gameType).equals(GameType.RANKED), pages);

        modelAndView.addObject("navpage", "games");
        return modelAndView;
    }

    @RequestMapping(value = "/games/{gameType}/{page}")
    @ResponseBody
    public GamesPlayedWrapper moreGames(@PathVariable int gameType, @PathVariable int page) {

        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(new PageRequest(page, 10), gameType);
        GamesPlayedWrapper wrapper = new GamesPlayedWrapper();
        wrapper.setRanked(GameType.getGameType(gameType).equals(GameType.RANKED));
        wrapper.setGames(gamesPlayed);

        return wrapper;
    }
}