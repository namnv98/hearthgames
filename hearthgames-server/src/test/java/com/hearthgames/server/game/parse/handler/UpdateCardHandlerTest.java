package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Card;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UpdateCardHandlerTest {

    @Test
    public void shouldUpdateCard() {

        UpdateCardHandler handler = new UpdateCardHandler();

        GameState gameState = new GameState();

        Card card = new Card();
        card.setEntityId("85");
        gameState.addCard(card);
        card = new Card();
        card.setEntityId("25");
        gameState.addCard(card);
        card = new Card();
        card.setEntityId("23");
        gameState.addCard(card);

        String[] lines = new String[11];
        lines[0] = "SHOW_ENTITY - Updating Entity=85 CardID=CS2_017o";
        lines[1] = "    tag=ATTACHED value=36".trim();
        lines[2] = "    tag=ZONE value=PLAY".trim();
        lines[3] = "    tag=CARDTYPE value=ENCHANTMENT".trim();
        lines[4] = "    tag=CREATOR value=37".trim();
        lines[5] = "SHOW_ENTITY - Updating Entity=[name=Emperor Thaurissan id=25 zone=DECK zonePos=0 cardId=BRM_028 player=1] CardID=BRM_028";
        lines[6] = "    tag=ZONE value=HAND".trim();
        lines[7] = "SHOW_ENTITY - Updating Entity=[id=23 cardId= type=INVALID zone=DECK zonePos=0 player=1] CardID=CS2_031";
        lines[8] = "    tag=COST value=1".trim();
        lines[9] = "    tag=ZONE value=HAND".trim();
        lines[10] = "something else";

        for (String line: lines) {
            if (handler.supports(gameState, line)) {
                LogLineData logLineData = new LogLineData(LocalDateTime.now().toString(), line);
                handler.handle(gameState, logLineData);
            }
        }

        assertEquals(3, gameState.getCards().size());
        assertTrue(gameState.getCards().containsKey("85"));
        assertTrue(gameState.getCards().containsKey("25"));
        assertTrue(gameState.getCards().containsKey("23"));
        assertEquals(3, gameState.getActivities().size());
        assertEquals("36", gameState.getActivities().get(0).getDelta().getAttached());
        assertEquals("HAND", gameState.getActivities().get(2).getDelta().getZone());
        assertEquals(false, gameState.isUpdateCard());
    }
}
