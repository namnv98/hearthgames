package com.hearthgames.server.database.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.database.domain.GamePlayed;

import java.io.IOException;

public class GamePlayedSerializer extends JsonSerializer<GamePlayed> {
    @Override
    public void serialize(GamePlayed value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("friendlyClass");
        g.writeString(value.getFriendlyClass().toLowerCase());
        g.writeFieldName("friendlyName");
        g.writeString(value.getFriendlyName());
        g.writeFieldName("opposingClass");
        g.writeString(value.getOpposingClass().toLowerCase());
        g.writeFieldName("opposingName");
        g.writeString(value.getOpposingName());
        g.writeFieldName("winnerClass");
        g.writeString(value.getWinnerClass().toLowerCase());
        g.writeFieldName("winner");
        g.writeString(value.getWinner());
        if (value.getRank() != null) {
            g.writeFieldName("rank");
            g.writeNumber(value.getRank());
        }
        g.writeFieldName("turns");
        g.writeNumber(value.getTurns());
        g.writeFieldName("id");
        g.writeNumber(value.getId());

        g.writeEndObject();
    }
}
