package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.LoggingAction;

import java.io.IOException;

public class LoggingActionSerializer extends JsonSerializer<LoggingAction> {

    @Override
    public void serialize(LoggingAction value, JsonGenerator g, SerializerProvider serializers) throws IOException, JsonProcessingException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("log");

        g.writeFieldName("msg");
        g.writeString(value.getMsg());

        g.writeEndObject();
    }
}
