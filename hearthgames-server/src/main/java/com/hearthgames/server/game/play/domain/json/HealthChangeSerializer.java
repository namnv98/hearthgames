package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.HealthChange;

import java.io.IOException;

public class HealthChangeSerializer extends JsonSerializer<HealthChange> {
    @Override
    public void serialize(HealthChange value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
