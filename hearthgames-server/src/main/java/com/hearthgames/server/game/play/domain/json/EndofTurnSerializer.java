package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.EndOfTurn;

import java.io.IOException;

public class EndofTurnSerializer extends JsonSerializer<EndOfTurn> {
    @Override
    public void serialize(EndOfTurn value, JsonGenerator g, SerializerProvider serializers) throws IOException, JsonProcessingException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("endOfTurn");

        g.writeEndObject();
    }
}
