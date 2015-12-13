package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Detached;

import java.io.IOException;

public class DetachedSerializer extends JsonSerializer<Detached> {
    @Override
    public void serialize(Detached value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("detached");

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getName());
            g.writeFieldName("id");
            g.writeString(value.getCard().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("detachedFrom");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDetachedFrom().getName());
            g.writeFieldName("id");
            g.writeString(value.getDetachedFrom().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getDetachedFrom().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeEndObject();
    }
}