package com.hearthlogs.web.log.parser.handler;

import static org.junit.Assert.*;

import com.hearthlogs.web.log.parser.handler.CreateCardHandler;
import com.hearthlogs.web.match.MatchContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateCardHandlerTest {

    @Test
    public void shouldCreateCard() {

        CreateCardHandler handler = new CreateCardHandler();

        MatchContext context = new MatchContext();

        String[] lines = new String[9];
        lines[0] = "FULL_ENTITY - Creating ID=86 CardID=FP1_002t";
        lines[1] = "    tag=EXHAUSTED value=1".trim();
        lines[2] = "    tag=ENTITY_ID value=86".trim();
        lines[3] = "    tag=CARDTYPE value=MINION".trim();
        lines[4] = "FULL_ENTITY - Creating ID=87 CardID=FP1_002t";
        lines[5] = "    tag=EXHAUSTED value=1".trim();
        lines[6] = "    tag=ENTITY_ID value=87".trim();
        lines[7] = "    tag=CARDTYPE value=MINION".trim();
        lines[8] = "something else";

        for (String line: lines) {
            if (handler.applies(context, line)) {
                handler.handle(context, line);
            }
        }

        assertEquals(2, context.getCards().size());
        assertEquals("1", context.getCards().get(0).getExhausted());
        assertEquals("MINION", context.getCards().get(0).getCardtype());
        assertEquals("86", context.getCards().get(0).getEntityId());

    }
}
