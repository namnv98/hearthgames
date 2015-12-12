package com.hearthlogs.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthlogs.server.game.play.domain.Trigger;

import java.io.IOException;

public class TriggerSerializer extends JsonSerializer<Trigger> {
    @Override
    public void serialize(Trigger value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
