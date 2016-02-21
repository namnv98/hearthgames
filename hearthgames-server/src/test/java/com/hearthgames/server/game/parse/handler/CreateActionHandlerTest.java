package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.GameEntity;
import com.hearthgames.server.game.parse.domain.Player;
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

        GameState gameState = new GameState();
        GameEntity gameEntity = new GameEntity();
        gameEntity.setState("RUNNING");
        gameState.setGameEntity(gameEntity);
        Player friendlyPlayer = new Player();
        friendlyPlayer.setName("Seekay");
        gameState.setFriendlyPlayer(friendlyPlayer);
        Player opposingPlayer = new Player();
        opposingPlayer.setName("Another Player");
        gameState.setOpposingPlayer(opposingPlayer);

        Card card = new Card();
        card.setEntityId("24");
        gameState.addCard(card);


        String[] lines = new String[7];
        lines[0] = "ACTION_START Entity=GameEntity BlockType=TRIGGER Index=-1 Target=0";
        lines[1]  = "    ACTION_START Entity=[id=24 cardId= type=INVALID zone=HAND zonePos=2 player=1] BlockType=POWER Index=1 Target=0".trim();
        lines[2] = "        filler".trim();
        lines[3] = "        filler".trim();
        lines[4] = "        filler".trim();
        lines[5] = "     ACTION_END".trim();
        lines[6] = "ACTION_END";

        for (String line: lines) {
            if (handler.supports(gameState, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(gameState, logLineData);
            }
        }

        assertEquals(1, gameState.getActivities().size());
        assertEquals(1, gameState.getActivities().get(0).getChildren().size());
    }

}