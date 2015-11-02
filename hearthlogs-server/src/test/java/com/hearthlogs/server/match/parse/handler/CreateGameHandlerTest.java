package com.hearthlogs.server.match.parse.handler;

import static org.junit.Assert.*;

import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateGameHandlerTest {

    @Test
    public void shouldCreateGame() {

        CreateGameHandler handler = new CreateGameHandler();

        ParsedMatch parsedMatch = new ParsedMatch();

        String[] lines = new String[5];
        lines[0] = "    GameEntity EntityID=1".trim();
        lines[1] = "        tag=TURN value=1".trim();
        lines[2] = "        tag=ENTITY_ID value=1".trim();
        lines[3] = "        tag=STATE value=RUNNING".trim();
        lines[4] = "something else";

        for (String line: lines) {
            if (handler.supports(parsedMatch, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(parsedMatch, logLineData);
            }
        }

        assertNotNull(parsedMatch.getGame());
        assertEquals("1", parsedMatch.getGame().getEntityId());
        assertEquals("1", parsedMatch.getGame().getTurn());
        assertEquals("RUNNING", parsedMatch.getGame().getState());
    }
}