package com.hearthlogs.server.game.parse.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.GameEntity;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.handler.CreateActionHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CreateActionHandlerTest {

    @Test
    public void shouldCreateAction() {

        CreateActionHandler handler = new CreateActionHandler();

        GameContext context = new GameContext();
        GameEntity gameEntity = new GameEntity();
        gameEntity.setState("RUNNING");
        context.setGameEntity(gameEntity);
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
        lines[0] = "ACTION_START Entity=GameEntity BlockType=TRIGGER Index=-1 Target=0";
        lines[1]  = "    ACTION_START Entity=[id=24 cardId= type=INVALID zone=HAND zonePos=2 player=1] BlockType=POWER Index=1 Target=0".trim();
        lines[2] = "        filler".trim();
        lines[3] = "        filler".trim();
        lines[4] = "        filler".trim();
        lines[5] = "     ACTION_END".trim();
        lines[6] = "ACTION_END";

        for (String line: lines) {
            if (handler.supports(context, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(context, logLineData);
            }
        }

        assertEquals(1, context.getActivities().size());
        assertEquals(1, context.getActivities().get(0).getChildren().size());
    }

}