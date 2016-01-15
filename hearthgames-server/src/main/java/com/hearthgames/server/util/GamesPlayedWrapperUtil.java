package com.hearthgames.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthgames.server.database.domain.GamePlayed;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class GamesPlayedWrapperUtil {

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
}
