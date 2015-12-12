package com.hearthlogs.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthlogs.server.game.play.domain.HeroPowerUsed;

import java.io.IOException;

public class HeroPowerUsedSerializer extends JsonSerializer<HeroPowerUsed> {
    @Override
    public void serialize(HeroPowerUsed value, JsonGenerator g, SerializerProvider serializers) throws IOException, JsonProcessingException {

    }
}
