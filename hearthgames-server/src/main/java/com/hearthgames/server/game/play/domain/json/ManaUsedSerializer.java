package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.ManaUsed;

import java.io.IOException;

public class ManaUsedSerializer extends JsonSerializer<ManaUsed> {
    @Override
    public void serialize(ManaUsed value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("manaUsed");

        g.writeFieldName("amount");
        g.writeNumber(value.getManaUsed());

        g.writeEndObject();
    }
}
