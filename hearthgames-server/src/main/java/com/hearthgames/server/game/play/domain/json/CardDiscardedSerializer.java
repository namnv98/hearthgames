package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.CardDiscarded;

import java.io.IOException;

public class CardDiscardedSerializer extends JsonSerializer<CardDiscarded> {
    @Override
    public void serialize(CardDiscarded value, JsonGenerator g, SerializerProvider serializers) throws IOException {

        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("cardDiscarded");

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getCard().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getCard().getEntityId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("cause");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCause().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getCause().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getCause().getEntityId());
            g.writeFieldName("rarity");
            g.writeString(value.getCause().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("cardController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCardController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getCardController().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("causeController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCauseController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getCauseController().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("player");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getPlayer().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getPlayer().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeEndObject();
    }
}
