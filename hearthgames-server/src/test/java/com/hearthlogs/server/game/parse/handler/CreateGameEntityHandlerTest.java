package com.hearthlogs.server.game.parse.handler;

import static org.junit.Assert.*;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.handler.CreateGameEntityHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class CreateGameEntityHandlerTest {

    @Test
    public void shouldCreateGame() {

        CreateGameEntityHandler handler = new CreateGameEntityHandler();

        GameContext context = new GameContext();

        String[] lines = new String[5];
        lines[0] = "    GameEntity EntityID=1".trim();
        lines[1] = "        tag=TURN value=1".trim();
        lines[2] = "        tag=ENTITY_ID value=1".trim();
        lines[3] = "        tag=STATE value=RUNNING".trim();
        lines[4] = "something else";

        for (String line: lines) {
            if (handler.supports(context, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(context, logLineData);
            }
        }

        assertNotNull(context.getGameEntity());
        assertEquals("1", context.getGameEntity().getEntityId());
        assertEquals("1", context.getGameEntity().getTurn());
        assertEquals("RUNNING", context.getGameEntity().getState());
    }
}