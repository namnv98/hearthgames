package com.hearthlogs.web.log.parser.handler;

import static org.junit.Assert.*;

import com.hearthlogs.web.log.parser.handler.CreateGameHandler;
import com.hearthlogs.web.match.MatchContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateGameHandlerTest {

    @Test
    public void shouldCreateGame() {

        CreateGameHandler handler = new CreateGameHandler();

        MatchContext context = new MatchContext();

        String[] lines = new String[5];
        lines[0] = "    GameEntity EntityID=1".trim();
        lines[1] = "        tag=TURN value=1".trim();
        lines[2] = "        tag=ENTITY_ID value=1".trim();
        lines[3] = "        tag=STATE value=RUNNING".trim();
        lines[4] = "something else";

        for (String line: lines) {
            if (handler.applies(context, line)) {
                handler.handle(context, line);
            }
        }

        assertNotNull(context.getGame());
        assertEquals("1", context.getGame().getEntityId());
        assertEquals("1", context.getGame().getTurn());
        assertEquals("RUNNING", context.getGame().getState());
    }
}