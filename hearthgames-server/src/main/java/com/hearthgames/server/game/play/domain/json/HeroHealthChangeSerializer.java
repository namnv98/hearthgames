package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.HeroHealthChange;

import java.io.IOException;

public class HeroHealthChangeSerializer extends JsonSerializer<HeroHealthChange> {
    @Override
    public void serialize(HeroHealthChange value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
