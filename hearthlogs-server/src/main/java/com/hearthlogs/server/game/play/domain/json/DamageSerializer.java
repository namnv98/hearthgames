package com.hearthlogs.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DamageSerializer extends JsonSerializer<DamageSerializer> {
    @Override
    public void serialize(DamageSerializer value, JsonGenerator g, SerializerProvider serializers) throws IOException, JsonProcessingException {

    }
}
