package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Kill;

import java.io.IOException;

public class KillSerializer extends JsonSerializer<Kill> {
    @Override
    public void serialize(Kill value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
