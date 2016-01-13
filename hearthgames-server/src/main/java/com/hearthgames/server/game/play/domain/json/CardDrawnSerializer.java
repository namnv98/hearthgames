package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.CardDrawn;

import java.io.IOException;

public class CardDrawnSerializer extends JsonSerializer<CardDrawn> {
    @Override
    public void serialize(CardDrawn value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("cardDrawn");

        g.writeFieldName("drawer");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDrawer().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getDrawer().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getCard().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getCard().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        if (value.getTrigger() != null) {
            g.writeFieldName("trigger");
            g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getTrigger().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getTrigger().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getTrigger().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getTrigger().getCardDetailsRarity().toLowerCase());
            g.writeEndObject();
        }

        g.writeEndObject();
    }
}
