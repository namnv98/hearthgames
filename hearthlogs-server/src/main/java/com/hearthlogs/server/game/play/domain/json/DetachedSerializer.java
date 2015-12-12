package com.hearthlogs.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthlogs.server.game.play.domain.Detached;

import java.io.IOException;

public class DetachedSerializer extends JsonSerializer<Detached> {
    @Override
    public void serialize(Detached value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
