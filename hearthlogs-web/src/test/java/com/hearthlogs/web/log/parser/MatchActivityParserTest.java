package com.hearthlogs.web.log.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.web.domain.Card;
import com.hearthlogs.web.domain.CardDetails;
import com.hearthlogs.web.domain.CardSets;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.CompletedMatch;
import com.hearthlogs.web.match.MatchContext;
import com.hearthlogs.web.service.CardService;
import com.hearthlogs.web.service.match.MatchService;
import com.hearthlogs.web.service.match.handler.*;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.net.URL;

@RunWith(MockitoJUnitRunner.class)
public class MatchActivityParserTest {

    @Test
    public void shouldPlaySampleGame1() throws IOException {

        CardService cardService = new CardService(new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class));

        AttackHandler attackHandler = new AttackHandler();
        attackHandler.setCardService(cardService);
        PowerHandler powerHandler = new PowerHandler();
        powerHandler.setCardService(cardService);
        CardHandler cardHandler = new CardHandler();
        cardHandler.setCardService(cardService);
        GameHandler gameHandler = new GameHandler();
        gameHandler.setCardService(cardService);
        PlayerHandler playerHandler = new PlayerHandler();
        playerHandler.setCardService(cardService);

        ActivityHandlers handlers = new ActivityHandlers(attackHandler, powerHandler, cardHandler, gameHandler, playerHandler);
        
        MatchService matchService = new MatchService(cardService, new MatchActivityParser(), null, null, handlers);


        String match = matchService.decompressGameData(getData("fullgame.test"));
//        FileUtils.writeStringToFile(new File("uncompressed-fullgame.test"), match, "UTF-8");

        String[] lines = match.split("\n");

        MatchActivityParser parser = new MatchActivityParser();
        MatchContext context = new MatchContext();
        for (String line: lines) {
            parser.parse(context, line);
        }
        for (Activity activity: context.getActivities()) {
            matchService.handle(context, activity);
        }

    }

    private byte[] getData(String filename) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(filename);
        assert url != null;
        File file = new File(url.getFile());
        return FileUtils.readFileToByteArray(file);
    }
}