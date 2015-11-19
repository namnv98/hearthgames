package com.hearthlogs.server.game.parse.handler;

import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.log.domain.LogLineData;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UpdatePlayerHandlerTest {

    @Test
    public void shouldUpdatePlayer() {

        UpdatePlayerHandler handler = new UpdatePlayerHandler();

        GameContext context = new GameContext();
        Player player1 = new Player();
        player1.setEntityId("2");
        player1.setTeamId("1");
        Player player2 = new Player();
        player2.setEntityId("3");
        player2.setTeamId("2");
        context.setFriendlyPlayer(player1);
        context.setOpposingPlayer(player2);

        String[] lines = new String[11];
        lines[0] = "TAG_CHANGE Entity=Player2 tag=TIMEOUT value=75";
        lines[1] = "TAG_CHANGE Entity=Player2 tag=CURRENT_PLAYER value=1";
        lines[2] = "TAG_CHANGE Entity=Player2 tag=FIRST_PLAYER value=1";
        lines[3] = "TAG_CHANGE Entity=Player2 tag=TEAM_ID value=2";
        lines[4] = "TAG_CHANGE Entity=Player2 tag=ENTITY_ID value=3";
        lines[5] = "TAG_CHANGE Entity=Player2 tag=NUM_CARDS_DRAWN_THIS_TURN value=3";
        lines[6] = "TAG_CHANGE Entity=Seekay tag=TIMEOUT value=75";
        lines[7] = "TAG_CHANGE Entity=Seekay tag=TEAM_ID value=1";
        lines[8] = "TAG_CHANGE Entity=Seekay tag=ENTITY_ID value=2";
        lines[9] = "TAG_CHANGE Entity=Seekay tag=NUM_CARDS_DRAWN_THIS_TURN value=4";
        lines[10] = "TAG_CHANGE Entity=GameEntity tag=10 value=85";

        for (String line : lines) {
            if (handler.supports(context, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(context, logLineData);
            }
        }

        assertEquals("2", context.getOpposingPlayer().getTeamId());
        assertEquals("Player2", context.getOpposingPlayer().getName());
        assertEquals("1", context.getOpposingPlayer().getFirstPlayer());
        assertEquals("1", context.getFriendlyPlayer().getTeamId());
        assertEquals("4", context.getFriendlyPlayer().getNumCardsDrawnThisTurn());
    }

}
