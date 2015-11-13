package com.hearthlogs.server.match.parse.handler;

import static org.junit.Assert.*;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.log.domain.LogLineData;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateGameHandlerTest {

    @Test
    public void shouldCreateGame() {

        CreateGameHandler handler = new CreateGameHandler();

        ParseContext context = new ParseContext();

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

        assertNotNull(context.getGame());
        assertEquals("1", context.getGame().getEntityId());
        assertEquals("1", context.getGame().getTurn());
        assertEquals("RUNNING", context.getGame().getState());
    }
}