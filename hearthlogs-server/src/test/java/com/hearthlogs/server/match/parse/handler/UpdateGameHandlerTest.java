package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.domain.Game;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UpdateGameHandlerTest {

    @Test
    public void shouldUpdateGame() {

        UpdateGameHandler handler = new UpdateGameHandler();

        ParsedMatch parsedMatch = new ParsedMatch();
        parsedMatch.setGame(new Game());

        String lines[] = new String[5];
        lines[0] = "TAG_CHANGE Entity=GameEntity tag=TURN value=1";
        lines[1] = "TAG_CHANGE Entity=GameEntity tag=STATE value=RUNNING";
        lines[2] = "TAG_CHANGE Entity=GameEntity tag=STEP value=FINAL_GAMEOVER";
        lines[3] = "TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE";
        lines[4] = "something else";

        for (String line: lines) {
            if (handler.supports(parsedMatch, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(parsedMatch, logLineData);
            }
        }

        assertEquals(2, parsedMatch.getActivities().size());
        assertEquals("1", parsedMatch.getGame().getTurn());
        assertEquals("RUNNING", parsedMatch.getGame().getState());
        assertEquals("COMPLETE", ((Game)parsedMatch.getActivities().get(1).getEntity()).getState());
    }
}