package com.hearthlogs.server.game.parse.handler;

import com.hearthlogs.server.game.parse.domain.GameEntity;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.log.domain.LogLineData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UpdateGameEntityHandlerTest {

    @Test
    public void shouldUpdateGame() {

        UpdateGameEntityHandler handler = new UpdateGameEntityHandler();

        GameContext context = new GameContext();
        context.setGameEntity(new GameEntity());

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
        assertEquals("1", context.getGameEntity().getTurn());
        assertEquals("RUNNING", context.getGameEntity().getState());
        assertEquals("COMPLETE", ((GameEntity) context.getActivities().get(1).getDelta()).getState());
    }
}