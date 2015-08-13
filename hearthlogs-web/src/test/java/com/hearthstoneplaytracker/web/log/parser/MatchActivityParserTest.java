package com.hearthlogs.web.log.parser;

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

//        CardService cardService = new CardService(new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class));
//
//        GameService gameService = new GameService(cardService, new GameActivityParser(), null, null);
//
//
//        String match = gameService.decompressGameData(getData("/temp/sample4.hpt"));
//        FileUtils.writeStringToFile(new File("uncompressed-sample4.txt"), match, "UTF-8");
//
//        String[] lines = match.split("\n");
//
//        GameActivityParser parser = new GameActivityParser();
//        GameContext context = new GameContext();
//        for (String line: lines) {
//            parser.parse(context, line);
//        }
//        for (Activity activity: context.getActivities()) {
//            gameService.handle(context, activity);
//        }
//
//        PlayedGame playedGame = context.getPlayedGame();
//        playedGame.setFriendlyPlayer(context.getFriendlyPlayer().getName());
//        playedGame.setOpposingPlayer(context.getOpposingPlayer().getName());
//
//        Card card = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
//        CardDetails cardDetails = cardService.getCardDetails(card.getCardid());
//        playedGame.setFriendlyClass(cardDetails.getPlayerClass());
//
//        card = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());
//        cardDetails = cardService.getCardDetails(card.getCardid());
//        playedGame.setOpposingClass(cardDetails.getPlayerClass());
//


        System.out.println();

    }


    private byte[] getData(String filename) throws IOException {
        URL url = this.getClass().getResource(filename);
        File file = new File(url.getFile());
        return FileUtils.readFileToByteArray(file);
    }


}
