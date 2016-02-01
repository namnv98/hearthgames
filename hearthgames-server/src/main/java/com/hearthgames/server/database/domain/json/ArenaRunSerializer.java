package com.hearthgames.server.database.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.database.domain.ArenaRun;

import java.io.IOException;

public class ArenaRunSerializer extends JsonSerializer<ArenaRun> {
    @Override
    public void serialize(ArenaRun value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("playerClass");
        g.writeString(value.getPlayerClass().toLowerCase());
        g.writeFieldName("playerName");
        g.writeString(value.getPlayerName());
        g.writeFieldName("wins");
        g.writeNumber(value.getWins());
        g.writeFieldName("losses");
        g.writeNumber(value.getLosses());
        g.writeFieldName("id");
        g.writeNumber(value.getArenaDeckId());

        g.writeEndObject();
    }
}
