package com.hearthgames.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthgames.server.database.domain.ArenaRun;
import com.hearthgames.server.database.domain.GamePlayed;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class WrapperUtil {

    public static void addGamesPlayed(ModelAndView modelAndView, List<GamePlayed> gamesPlayed, boolean ranked, int pages) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            GamesPlayedWrapper wrapper = new GamesPlayedWrapper();
            wrapper.setRanked(ranked);
            wrapper.setPages(pages);
            wrapper.setGames(gamesPlayed);
            String jsonInString = mapper.writeValueAsString(wrapper);
            modelAndView.addObject("gamesPlayed", jsonInString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void addArenaRuns(ModelAndView modelAndView, List<ArenaRun> arenaRuns, int pages) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArenaRunWrapper wrapper = new ArenaRunWrapper();
            wrapper.setPages(pages);
            wrapper.setRuns(arenaRuns);
            String jsonInString = mapper.writeValueAsString(wrapper);
            modelAndView.addObject("arenaRuns", jsonInString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
