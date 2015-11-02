package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Game;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CreateActionHandlerTest {

    @Test
    public void shouldCreateAction() {

        CreateActionHandler handler = new CreateActionHandler();

        ParsedMatch parsedMatch = new ParsedMatch();
        Game game = new Game();
        game.setState("RUNNING");
        parsedMatch.setGame(game);
        Player friendlyPlayer = new Player();
        friendlyPlayer.setName("Seekay");
        parsedMatch.setFriendlyPlayer(friendlyPlayer);
        Player opposingPlayer = new Player();
        opposingPlayer.setName("Another Player");
        parsedMatch.setOpposingPlayer(opposingPlayer);

        Card card = new Card();
        card.setEntityId("24");
        parsedMatch.getCards().add(card);


        String[] lines = new String[7];
        lines[0] = "ACTION_START Entity=GameEntity BlockType=TRIGGER Index=-1 Target=0";
        lines[1]  = "    ACTION_START Entity=[id=24 cardId= type=INVALID zone=HAND zonePos=2 player=1] BlockType=POWER Index=1 Target=0".trim();
        lines[2] = "        filler".trim();
        lines[3] = "        filler".trim();
        lines[4] = "        filler".trim();
        lines[5] = "     ACTION_END".trim();
        lines[6] = "ACTION_END";

        for (String line: lines) {
            if (handler.supports(parsedMatch, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(parsedMatch, logLineData);
            }
        }

        assertEquals(1, parsedMatch.getActivities().size());
        assertEquals(1, parsedMatch.getActivities().get(0).getChildren().size());
        assertEquals(false, parsedMatch.isCreateAction());
    }

}