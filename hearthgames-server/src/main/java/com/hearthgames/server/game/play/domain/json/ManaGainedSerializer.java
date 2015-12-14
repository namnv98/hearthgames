package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.ManaGained;

import java.io.IOException;

public class ManaGainedSerializer extends JsonSerializer<ManaGained> {
    @Override
    public void serialize(ManaGained value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("manaGained");

        g.writeFieldName("player");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getPlayer().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getPlayer().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("amount");
        g.writeNumber(value.getManaGained());

        g.writeEndObject();
    }
}
