package com.hearthlogs.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthlogs.server.game.play.domain.ManaLost;

import java.io.IOException;

public class ManaLostSerializer extends JsonSerializer<ManaLost> {
    @Override
    public void serialize(ManaLost value, JsonGenerator g, SerializerProvider serializers) throws IOException, JsonProcessingException {

    }
}
