package com.hearthlogs.server.game.parse.handler;

import com.hearthgames.server.game.parse.domain.GameEntity;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.handler.TagChangeHandler;
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

        GameContext context = new GameContext();
        context.setGameEntity(new GameEntity());

        String line1 = "TAG_CHANGE Entity=GameEntity tag=TURN value=1";
        if (handler.supports(context, line1)) {
            LogLineData logLineData1 = new LogLineData(LocalDateTime.now().toString(), line1);
            handler.handle(context, logLineData1);
        }
        String line2 = "TAG_CHANGE Entity=GameEntity tag=ZONE value=PLAY";
        context.getActivityStack().push(new Activity());
        if (handler.supports(context, line2)) {
            LogLineData logLineData2 = new LogLineData(LocalDateTime.now().toString(), line2);
            handler.handle(context, logLineData2);
        }

        assertEquals(1, context.getActivities().size());
        assertEquals("1", ((GameEntity) context.getActivities().get(0).getDelta()).getTurn());
        assertEquals(1, context.getActivityStack().size());
        assertEquals("PLAY", ((GameEntity) context.getActivityStack().peek().getChildren().get(0).getDelta()).getZone());


    }
}
