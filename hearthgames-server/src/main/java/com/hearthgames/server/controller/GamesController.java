package com.hearthgames.server.controller;

import com.hearthgames.server.database.domain.ArenaRun;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.log.domain.GameType;
import com.hearthgames.server.util.ArenaRunWrapper;
import com.hearthgames.server.util.GamesPlayedWrapper;
import com.hearthgames.server.util.WrapperUtil;
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

    private static final String GAMES = "games";
    private static final String ARENARUNS = "arenaruns";

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/account/{gameAccountId}/games")
    public ModelAndView listGames(@PathVariable String gameAccountId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(GAMES);

        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(gameAccountId);
        Long count = gameService.getGamesPlayedCount(gameAccountId);
        int pages = (int) Math.ceil((double) count / 10);

        WrapperUtil.addGamesPlayed(modelAndView, gamesPlayed, true, pages);
        return modelAndView;
    }

    @RequestMapping(value = "/games/{gameType}")
    public ModelAndView listGames(@PathVariable int gameType) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(GAMES);

        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(new PageRequest(0, 10), gameType);
        Long count = gameService.getGamesPlayedCount(gameType);
        int pages = (int) Math.ceil((double) count / 10);

        WrapperUtil.addGamesPlayed(modelAndView, gamesPlayed, GameType.getGameType(gameType).equals(GameType.RANKED), pages);

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

    @RequestMapping(value = "/games/arena")
    @ResponseBody
    public ModelAndView arenaRuns() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ARENARUNS);

        List<ArenaRun> arenaRuns = gameService.getArenaRuns(new PageRequest(0, 10));
        Long count = gameService.getArenaRunCount();
        int pages = (int) Math.ceil((double) count / 10);

        WrapperUtil.addArenaRuns(modelAndView, arenaRuns, pages);

        modelAndView.addObject("navpage", "games");
        return modelAndView;
    }

    @RequestMapping(value = "/games/arena/{page}")
    @ResponseBody
    public ArenaRunWrapper moreArenaRuns(@PathVariable int page) {

        List<ArenaRun> arenaRuns = gameService.getArenaRuns(new PageRequest(page, 10));
        ArenaRunWrapper wrapper = new ArenaRunWrapper();
        wrapper.setRuns(arenaRuns);

        return wrapper;
    }

    @RequestMapping(value = "/games/arenarun/{arenaDeckId}")
    public ModelAndView listArenaGames(@PathVariable  String arenaDeckId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("arenagames");

        List<GamePlayed> gamesPlayed = gameService.getArenaGamesByDeckId(arenaDeckId);
        int count = gamesPlayed.size();
        int pages = (int) Math.ceil((double) count / 10);

        WrapperUtil.addGamesPlayed(modelAndView, gamesPlayed, false, pages);

        modelAndView.addObject("navpage", "games");
        return modelAndView;
    }


}
