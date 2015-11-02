package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.domain.Game;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;
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

        ParsedMatch parsedMatch = new ParsedMatch();
        parsedMatch.setGame(new Game());

        String line1 = "TAG_CHANGE Entity=GameEntity tag=TURN value=1";
        if (handler.supports(parsedMatch, line1)) {
            LogLineData logLineData1 = new LogLineData(LocalDateTime.now().toString(), line1);
            handler.handle(parsedMatch, logLineData1);
        }
        String line2 = "TAG_CHANGE Entity=GameEntity tag=ZONE value=PLAY";
        parsedMatch.getActivityStack().push(new Activity());
        if (handler.supports(parsedMatch, line2)) {
            LogLineData logLineData2 = new LogLineData(LocalDateTime.now().toString(), line2);
            handler.handle(parsedMatch, logLineData2);
        }

        assertEquals(1, parsedMatch.getActivities().size());
        assertEquals("1", ((Game)parsedMatch.getActivities().get(0).getEntity()).getTurn());
        assertEquals(1, parsedMatch.getActivityStack().size());
        assertEquals("PLAY", ((Game) parsedMatch.getActivityStack().peek().getChildren().get(0).getEntity()).getZone());


    }
}
