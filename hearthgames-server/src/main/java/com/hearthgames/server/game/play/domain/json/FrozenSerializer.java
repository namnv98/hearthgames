package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Frozen;

import java.io.IOException;

public class FrozenSerializer extends JsonSerializer<Frozen> {
    @Override
    public void serialize(Frozen value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("frozen");

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getName());
            g.writeFieldName("id");
            g.writeString(value.getCard().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("cardController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCardController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getCardController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("frozen");
        g.writeBoolean(value.isFrozen());

        g.writeEndObject();
    }
}
