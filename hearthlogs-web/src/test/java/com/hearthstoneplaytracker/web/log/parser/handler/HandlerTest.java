package com.hearthlogs.web.log.parser.handler;

import static org.junit.Assert.*;

import com.hearthlogs.web.log.parser.handler.Handler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {

    private Handler handler = Mockito.spy(Handler.class);

    @Test
    public void shouldParseTagChangeWithNamedCard() {
        String entity = handler.parseEntityStr("TAG_CHANGE Entity=[name=Antique Healbot id=16 zone=HAND zonePos=4 cardId=GVG_069 player=1] tag=ZONE_POSITION value=0");
        assertEquals("16", entity);
    }

    @Test
    public void shouldParseTagChangeWithOpposingPlayerCard() {
        String entity = handler.parseEntityStr("TAG_CHANGE Entity=[id=46 cardId= type=INVALID zone=DECK zonePos=0 player=2] tag=ZONE_POSITION value=1");
        assertEquals(entity, "46");
    }

    @Test
    public void shouldParseTagChangeWithPlayerName() {
        String entity = handler.parseEntityStr("TAG_CHANGE Entity=Player with Spaces tag=MULLIGAN_STATE value=WAITING");
        assertEquals("Player with Spaces", entity);
    }

    @Test
    public void shouldParseEntityWithNameOnly() {
        String entity = handler.parseEntity("[name=Antique Healbot id=16 zone=HAND zonePos=4 cardId=GVG_069 player=1]");
        assertEquals("16", entity);
    }
}
