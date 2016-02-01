package com.hearthgames.server.controller;

import com.hearthgames.server.config.security.UserInfo;
import com.hearthgames.server.database.domain.Account;
import com.hearthgames.server.database.domain.ArenaRun;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@PreAuthorize("hasRole('USER')")
public class DashboardController {

    private static final String DASHBOARD = "dashboard";

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/dashboard")
    public ModelAndView dashboard() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(DASHBOARD);

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = gameService.getAccount(userInfo.getBattletag());

        modelAndView.addObject("account", account);

        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(account.getGameAccountId());
        List<ArenaRun> arenaRuns = gameService.getArenaRuns(account.getGameAccountId());

        modelAndView.addObject("gamesPlayed", gamesPlayed);
        modelAndView.addObject("arenaRuns", arenaRuns);
        modelAndView.addObject("navpage", "dashboard");

        return modelAndView;
    }


    @RequestMapping(value = "/dashboard/update")
    public ModelAndView dashboard(@RequestParam(name = "gameAccountId") String gameAccountId) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(DASHBOARD);

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = gameService.getAccount(userInfo.getBattletag());
        account.setGameAccountId(gameAccountId);
        gameService.updateGameAccountId(userInfo.getBattletag(), gameAccountId);

        modelAndView.addObject("updated", true);
        modelAndView.addObject("account", account);

        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(gameAccountId);

        modelAndView.addObject("gamesPlayed", gamesPlayed);

        return modelAndView;
    }
}