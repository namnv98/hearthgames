package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.domain.Game;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.log.domain.LogLineData;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TagChangeHandlerTest {

    @Test
    public void shouldUpdateTag() {

        TagChangeHandler handler = new TagChangeHandler();

        ParseContext context = new ParseContext();
        context.setGame(new Game());

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
        assertEquals("1", ((Game) context.getActivities().get(0).getDelta()).getTurn());
        assertEquals(1, context.getActivityStack().size());
        assertEquals("PLAY", ((Game) context.getActivityStack().peek().getChildren().get(0).getDelta()).getZone());


    }
}
