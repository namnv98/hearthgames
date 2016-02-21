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
public class CreateGameEntityHandlerTest {

    @Test
    public void shouldCreateGame() {

        CreateGameEntityHandler handler = new CreateGameEntityHandler();

        GameState gameState = new GameState();

        String[] lines = new String[5];
        lines[0] = "    GameEntity EntityID=1".trim();
        lines[1] = "        tag=TURN value=1".trim();
        lines[2] = "        tag=ENTITY_ID value=1".trim();
        lines[3] = "        tag=STATE value=RUNNING".trim();
        lines[4] = "something else";

        for (String line: lines) {
            if (handler.supports(gameState, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(gameState, logLineData);
            }
        }

        assertNotNull(gameState.getGameEntity());
        assertEquals("1", gameState.getGameEntity().getEntityId());
        assertEquals("1", gameState.getGameEntity().getTurn());
        assertEquals("RUNNING", gameState.getGameEntity().getState());
    }
}