package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.GameEntity;
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

        GameState gameState = new GameState();
        gameState.setGameEntity(new GameEntity());

        String lines[] = new String[5];
        lines[0] = "TAG_CHANGE Entity=GameEntity tag=TURN value=1";
        lines[1] = "TAG_CHANGE Entity=GameEntity tag=STATE value=RUNNING";
        lines[2] = "TAG_CHANGE Entity=GameEntity tag=STEP value=FINAL_GAMEOVER";
        lines[3] = "TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE";
        lines[4] = "something else";

        for (String line: lines) {
            if (handler.supports(gameState, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(gameState, logLineData);
            }
        }

        assertEquals(2, gameState.getActivities().size());
        assertEquals("1", gameState.getGameEntity().getTurn());
        assertEquals("RUNNING", gameState.getGameEntity().getState());
        assertEquals("COMPLETE", ((GameEntity) gameState.getActivities().get(1).getDelta()).getState());
    }
}