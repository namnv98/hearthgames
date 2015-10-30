package com.hearthlogs.server.service.log.handler;

import com.hearthlogs.server.match.domain.Game;
import com.hearthlogs.server.match.MatchContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UpdateGameHandlerTest {

    @Test
    public void shouldUpdateGame() {

        UpdateGameHandler handler = new UpdateGameHandler();

        MatchContext context = new MatchContext();
        context.setGame(new Game());

        String lines[] = new String[5];
        lines[0] = "TAG_CHANGE Entity=GameEntity tag=TURN value=1";
        lines[1] = "TAG_CHANGE Entity=GameEntity tag=STATE value=RUNNING";
        lines[2] = "TAG_CHANGE Entity=GameEntity tag=STEP value=FINAL_GAMEOVER";
        lines[3] = "TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE";
        lines[4] = "something else";

        for (String line: lines) {
            if (handler.applies(context, line)) {
                handler.handle(context, line);
            }
        }

        assertEquals(2, context.getActivities().size());
        assertEquals("1", context.getGame().getTurn());
        assertEquals("RUNNING", context.getGame().getState());
        assertEquals("COMPLETE", ((Game)context.getActivities().get(1).getEntity()).getState());
    }
}