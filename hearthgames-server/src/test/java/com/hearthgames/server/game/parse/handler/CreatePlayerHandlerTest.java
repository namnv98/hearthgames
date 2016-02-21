package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class CreatePlayerHandlerTest {

    @Test
    public void shouldCreatePlayer() {

        CreatePlayerHandler handler = new CreatePlayerHandler();

        GameState gameState = new GameState();

        String[] lines = new String[33];
        lines[0]  = "Player EntityID=2 PlayerID=1 GameAccountId=[hi=144115193835963207 lo=35548956]";
        lines[1]  = "    tag=TIMEOUT value=75".trim();
        lines[2]  = "    tag=PLAYSTATE value=PLAYING".trim();
        lines[3]  = "    tag=HERO_ENTITY value=4".trim();
        lines[4]  = "    tag=MAXHANDSIZE value=10".trim();
        lines[5]  = "    tag=STARTHANDSIZE value=4".trim();
        lines[6]  = "    tag=PLAYER_ID value=1".trim();
        lines[7]  = "    tag=TEAM_ID value=1".trim();
        lines[8]  = "    tag=ZONE value=PLAY".trim();
        lines[9]  = "    tag=CONTROLLER value=1".trim();
        lines[10] = "    tag=ENTITY_ID value=2".trim();
        lines[11] = "    tag=MAXRESOURCES value=10".trim();
        lines[12] = "    tag=CARDTYPE value=PLAYER".trim();
        lines[13] = "    tag=NUM_TURNS_LEFT value=1".trim();
        lines[14] = "    tag=NUM_CARDS_DRAWN_THIS_TURN value=4".trim();
        lines[15] = "Player EntityID=3 PlayerID=2 GameAccountId=[hi=144115193835963207 lo=26973114]";
        lines[16] = "    tag=TIMEOUT value=75".trim();
        lines[17] = "    tag=PLAYSTATE value=PLAYING".trim();
        lines[18] = "    tag=CURRENT_PLAYER value=1".trim();
        lines[19] = "    tag=FIRST_PLAYER value=1".trim();
        lines[20] = "    tag=HERO_ENTITY value=36".trim();
        lines[21] = "    tag=MAXHANDSIZE value=10".trim();
        lines[22] = "    tag=STARTHANDSIZE value=4".trim();
        lines[23] = "    tag=PLAYER_ID value=2".trim();
        lines[24] = "    tag=TEAM_ID value=2".trim();
        lines[25] = "    tag=ZONE value=PLAY".trim();
        lines[26] = "    tag=CONTROLLER value=2".trim();
        lines[27] = "    tag=ENTITY_ID value=3".trim();
        lines[28] = "    tag=MAXRESOURCES value=10".trim();
        lines[29] = "    tag=CARDTYPE value=PLAYER".trim();
        lines[30] = "    tag=NUM_TURNS_LEFT value=1".trim();
        lines[31] = "    tag=NUM_CARDS_DRAWN_THIS_TURN value=3".trim();
        lines[32] = "something else";

        for (String line: lines) {
            if (handler.supports(gameState, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(gameState, logLineData);
            }
        }

        assertNotNull(gameState.getFriendlyPlayer());
        assertNotNull(gameState.getOpposingPlayer());
        assertEquals("35548956", gameState.getFriendlyPlayer().getGameAccountIdLo());
        assertEquals("26973114", gameState.getOpposingPlayer().getGameAccountIdLo());
        assertEquals("1", gameState.getOpposingPlayer().getFirstPlayer());
        assertEquals("1", gameState.getFriendlyPlayer().getTeamId());
        assertEquals("2", gameState.getOpposingPlayer().getTeamId());
        assertEquals("4", gameState.getFriendlyPlayer().getNumCardsDrawnThisTurn());
        assertEquals("3", gameState.getOpposingPlayer().getNumCardsDrawnThisTurn());

    }
}
