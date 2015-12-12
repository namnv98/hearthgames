package com.hearthlogs.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthlogs.server.game.play.domain.TempManaGained;

import java.io.IOException;

public class TempManaGainedSerializer extends JsonSerializer<TempManaGained> {
    @Override
    public void serialize(TempManaGained value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
