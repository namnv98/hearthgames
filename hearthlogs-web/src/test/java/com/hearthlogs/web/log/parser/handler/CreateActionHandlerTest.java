package com.hearthlogs.web.log.parser.handler;

import static org.junit.Assert.*;

import com.hearthlogs.web.domain.Card;
import com.hearthlogs.web.domain.Game;
import com.hearthlogs.web.domain.Player;
import com.hearthlogs.web.log.parser.handler.CreateActionHandler;
import com.hearthlogs.web.match.MatchContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateActionHandlerTest {

    @Test
    public void shouldCreateAction() {

        CreateActionHandler handler = new CreateActionHandler();

        MatchContext context = new MatchContext();
        Game game = new Game();
        game.setState("RUNNING");
        context.setGame(game);
        Player friendlyPlayer = new Player();
        friendlyPlayer.setName("Seekay");
        context.setFriendlyPlayer(friendlyPlayer);
        Player opposingPlayer = new Player();
        opposingPlayer.setName("Another Player");
        context.setOpposingPlayer(opposingPlayer);

        Card card = new Card();
        card.setEntityId("24");
        context.getCards().add(card);


        String[] lines = new String[7];
        lines[0] = "ACTION_START Entity=GameEntity SubType=TRIGGER Index=-1 Target=0";
        lines[1]  = "    ACTION_START Entity=[id=24 cardId= type=INVALID zone=HAND zonePos=2 player=1] SubType=POWER Index=1 Target=0".trim();
        lines[2] = "        filler".trim();
        lines[3] = "        filler".trim();
        lines[4] = "        filler".trim();
        lines[5] = "     ACTION_END".trim();
        lines[6] = "ACTION_END";

        for (String line: lines) {
            if (handler.applies(context, line)) {
                handler.handle(context, line);
            }
        }

        assertEquals(1, context.getActivities().size());
        assertEquals(1, context.getActivities().get(0).getChildren().size());
        assertEquals(false, context.isCreateAction());
    }

}