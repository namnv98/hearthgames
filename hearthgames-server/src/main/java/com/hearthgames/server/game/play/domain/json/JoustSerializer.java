package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Joust;

import java.io.IOException;

public class JoustSerializer extends JsonSerializer<Joust> {
    @Override
    public void serialize(Joust value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("joust");

        g.writeFieldName("friendlyJouster");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getFriendlyJouster().getName());
            g.writeFieldName("id");
            g.writeString(value.getFriendlyJouster().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getFriendlyJouster().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("opposingJouster");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getOpposingJouster().getName());
            g.writeFieldName("id");
            g.writeString(value.getOpposingJouster().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getOpposingJouster().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("friendly");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getFriendly().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getFriendly().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("opposing");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getOpposing().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getOpposing().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("winner");
        g.writeBoolean(value.isWinner());

        g.writeEndObject();

    }
}
