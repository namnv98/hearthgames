package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Frozen;

import java.io.IOException;

public class FrozenSerializer extends JsonSerializer<Frozen> {
    @Override
    public void serialize(Frozen value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
