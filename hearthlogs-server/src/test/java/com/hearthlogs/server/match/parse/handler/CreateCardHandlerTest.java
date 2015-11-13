package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.log.domain.LogLineData;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CreateCardHandlerTest {

    @Test
    public void shouldCreateCard() {

        CreateCardHandler handler = new CreateCardHandler();

        ParseContext context = new ParseContext();

        Player player = new Player();
        player.setTeamId("2");
        context.setFriendlyPlayer(player);

        player = new Player();
        player.setTeamId("1");
        context.setOpposingPlayer(player);


        String[] lines = new String[9];
        lines[0] = "FULL_ENTITY - Creating ID=86 CardID=FP1_002t";
        lines[1] = "    tag=CONTROLLER value=1".trim();
        lines[2] = "    tag=ENTITY_ID value=86".trim();
        lines[3] = "    tag=CARDTYPE value=MINION".trim();
        lines[4] = "FULL_ENTITY - Creating ID=87 CardID=FP1_002t";
        lines[5] = "    tag=CONTROLLER value=1".trim();
        lines[6] = "    tag=ENTITY_ID value=87".trim();
        lines[7] = "    tag=CARDTYPE value=MINION".trim();
        lines[8] = "something else";

        for (String line: lines) {
            if (handler.supports(context, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(context, logLineData);
            }
        }

        assertEquals(2, context.getCards().size());
        assertEquals("1", context.getCards().get(0).getController());
        assertEquals("MINION", context.getCards().get(0).getCardtype());
        assertEquals("86", context.getCards().get(0).getEntityId());
        assertEquals("1", context.getFriendlyPlayer().getTeamId());
        assertEquals("2", context.getOpposingPlayer().getTeamId());

    }
}
