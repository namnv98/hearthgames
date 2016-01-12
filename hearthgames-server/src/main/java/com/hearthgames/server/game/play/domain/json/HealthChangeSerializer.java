package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.HealthChange;

import java.io.IOException;

public class HealthChangeSerializer extends JsonSerializer<HealthChange> {
    @Override
    public void serialize(HealthChange value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("healthChange");

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getName());
            g.writeFieldName("cardId");
            g.writeString(value.getCard().getCardDetails().getId());
            g.writeFieldName("id");
            g.writeString(value.getCard().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetails().getRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("cardController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCardController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getCardController().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("amount");
        g.writeNumber(value.getAmount());

        g.writeFieldName("health");
        g.writeNumber(value.getNewHealth());

        g.writeEndObject();

    }
}
