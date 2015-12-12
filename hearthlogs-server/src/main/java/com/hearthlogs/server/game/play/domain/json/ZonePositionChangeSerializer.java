package com.hearthlogs.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthlogs.server.game.play.domain.ZonePositionChange;

import java.io.IOException;

public class ZonePositionChangeSerializer extends JsonSerializer<ZonePositionChange> {
    @Override
    public void serialize(ZonePositionChange value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
