package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.NumOptions;

import java.io.IOException;

public class NumOptionsSerializer extends JsonSerializer<NumOptions> {
    @Override
    public void serialize(NumOptions value, JsonGenerator g, SerializerProvider serializers) throws IOException, JsonProcessingException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("numOptions");

        g.writeFieldName("number");
        g.writeNumber(value.getNumber());

        g.writeEndObject();
    }
}
