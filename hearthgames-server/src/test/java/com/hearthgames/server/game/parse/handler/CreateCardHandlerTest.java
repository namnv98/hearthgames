package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CreateCardHandlerTest {

    @Test
    public void shouldCreateCard() {

        CreateCardHandler handler = new CreateCardHandler();

        GameState gameState = new GameState();

        Player player = new Player();
        player.setTeamId("2");
        gameState.setFriendlyPlayer(player);

        player = new Player();
        player.setTeamId("1");
        gameState.setOpposingPlayer(player);


        String[] lines = new String[9];
        lines[0] = "FULL_ENTITY - Creating ID=86 CardID=FP1_002t";
        lines[1] = "    tag=CONTROLLER value=1".trim();
        lines[2] = "    tag=ENTITY_ID value=86".trim();
        lines[3] = "    tag=CARDTYPE value=MINION".trim();
        lines[4] = "FULL_ENTITY - Creating ID=87 CardID=FP1_002t";
        lines[5] = "    tag=CONTROLLER value=1".trim();
        lines[6] = "    tag=ENTITY_ID value=87".trim();
        lines[7] = "    tag=CARDTYPE value=MINION".trim();
        lines[8] = "something else";

        for (String line: lines) {
            if (handler.supports(gameState, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(gameState, logLineData);
            }
        }

        assertEquals(2, gameState.getCards().size());
        Card card = gameState.getCards().values().iterator().next();
        assertEquals("1", card.getController());
        assertEquals("MINION", card.getCardtype());
        assertEquals("86", card.getEntityId());
        assertEquals("1", gameState.getFriendlyPlayer().getTeamId());
        assertEquals("2", gameState.getOpposingPlayer().getTeamId());

    }
}
