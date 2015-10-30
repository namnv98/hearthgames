package com.hearthlogs.server.service.log.handler;

import com.hearthlogs.server.match.domain.Game;
import com.hearthlogs.server.match.domain.Activity;
import com.hearthlogs.server.match.MatchContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TagChangeHandlerTest {

    @Test
    public void shouldUpdateTag() {

        TagChangeHandler handler = new TagChangeHandler();

        MatchContext context = new MatchContext();
        context.setGame(new Game());

        String line1 = "TAG_CHANGE Entity=GameEntity tag=TURN value=1";
        if (handler.applies(context, line1)) {
            handler.handle(context, line1);
        }
        String line2 = "TAG_CHANGE Entity=GameEntity tag=ZONE value=PLAY";
        context.getActivityStack().push(new Activity());
        if (handler.applies(context, line2)) {
            handler.handle(context, line2);
        }

        assertEquals(1, context.getActivities().size());
        assertEquals("1", ((Game)context.getActivities().get(0).getEntity()).getTurn());
        assertEquals(1, context.getActivityStack().size());
        assertEquals("PLAY", ((Game) context.getActivityStack().peek().getChildren().get(0).getEntity()).getZone());


    }
}
