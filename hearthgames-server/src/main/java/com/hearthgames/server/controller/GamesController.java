package com.hearthgames.server.controller;

import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
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

        modelAndView.addObject("gamesType", new GamesType(false, false, true));
        modelAndView.addObject("gamesPlayed", gamesPlayed);
        return modelAndView;
    }

    @RequestMapping(value = "/casualgames")
    public ModelAndView listCasualGames() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("games");

        Iterable<GamePlayed> gamesPlayed = gameService.getCasualGamesPlayed();

        modelAndView.addObject("gamesType", new GamesType(true, false, false));
        modelAndView.addObject("gamesPlayed", gamesPlayed);
        return modelAndView;
    }

    @RequestMapping(value = "/rankedgames")
    public ModelAndView listRankedGames() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("games");

        Iterable<GamePlayed> gamesPlayed = gameService.getRankedGamesPlayed();

        modelAndView.addObject("gamesType", new GamesType(false, true, false));
        modelAndView.addObject("gamesPlayed", gamesPlayed);
        return modelAndView;
    }


    public static class GamesType {

        private boolean casual;
        private boolean ranked;
        private boolean account;

        public GamesType(boolean casual, boolean ranked, boolean account) {
            this.casual = casual;
            this.ranked = ranked;
            this.account = account;
        }

        public boolean isCasual() {
            return casual;
        }

        public void setCasual(boolean casual) {
            this.casual = casual;
        }

        public boolean isRanked() {
            return ranked;
        }

        public void setRanked(boolean ranked) {
            this.ranked = ranked;
        }

        public boolean isAccount() {
            return account;
        }

        public void setAccount(boolean account) {
            this.account = account;
        }
    }
}