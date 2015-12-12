package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.ManaGained;

import java.io.IOException;

public class ManaGainedSerializer extends JsonSerializer<ManaGained> {
    @Override
    public void serialize(ManaGained value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
