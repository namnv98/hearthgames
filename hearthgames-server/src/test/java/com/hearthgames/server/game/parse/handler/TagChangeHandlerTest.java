package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.GameEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TagChangeHandlerTest {

    @Test
    public void shouldUpdateTag() {

        TagChangeHandler handler = new TagChangeHandler();

        GameState gameState = new GameState();
        gameState.setGameEntity(new GameEntity());

        String line1 = "TAG_CHANGE Entity=GameEntity tag=TURN value=1";
        if (handler.supports(gameState, line1)) {
            LogLineData logLineData1 = new LogLineData(LocalDateTime.now().toString(), line1);
            handler.handle(gameState, logLineData1);
        }
        String line2 = "TAG_CHANGE Entity=GameEntity tag=ZONE value=PLAY";
        gameState.getActivityStack().push(new Activity());
        if (handler.supports(gameState, line2)) {
            LogLineData logLineData2 = new LogLineData(LocalDateTime.now().toString(), line2);
            handler.handle(gameState, logLineData2);
        }

        assertEquals(1, gameState.getActivities().size());
        assertEquals("1", ((GameEntity) gameState.getActivities().get(0).getDelta()).getTurn());
        assertEquals(1, gameState.getActivityStack().size());
        assertEquals("PLAY", gameState.getActivityStack().peek().getChildren().get(0).getDelta().getZone());


    }
}
