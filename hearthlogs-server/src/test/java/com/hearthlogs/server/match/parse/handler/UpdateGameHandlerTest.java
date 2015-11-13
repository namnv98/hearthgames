package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.domain.Game;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.log.domain.LogLineData;
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

        ParseContext context = new ParseContext();
        context.setGame(new Game());

        String lines[] = new String[5];
        lines[0] = "TAG_CHANGE Entity=GameEntity tag=TURN value=1";
        lines[1] = "TAG_CHANGE Entity=GameEntity tag=STATE value=RUNNING";
        lines[2] = "TAG_CHANGE Entity=GameEntity tag=STEP value=FINAL_GAMEOVER";
        lines[3] = "TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE";
        lines[4] = "something else";

        for (String line: lines) {
            if (handler.supports(context, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(context, logLineData);
            }
        }

        assertEquals(2, context.getActivities().size());
        assertEquals("1", context.getGame().getTurn());
        assertEquals("RUNNING", context.getGame().getState());
        assertEquals("COMPLETE", ((Game) context.getActivities().get(1).getDelta()).getState());
    }
}