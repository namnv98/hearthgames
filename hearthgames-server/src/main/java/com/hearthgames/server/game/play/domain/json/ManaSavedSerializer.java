package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.ManaSaved;

import java.io.IOException;

public class ManaSavedSerializer extends JsonSerializer<ManaSaved> {
    @Override
    public void serialize(ManaSaved value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("manaSaved");

        g.writeFieldName("amount");
        g.writeNumber(value.getAmount());


        g.writeEndObject();
    }
}
