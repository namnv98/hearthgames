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
            g.writeString(value.getFriendlyJouster().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getFriendlyJouster().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getFriendlyJouster().getEntityId());
            g.writeFieldName("rarity");
            g.writeString(value.getFriendlyJouster().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("opposingJouster");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getOpposingJouster().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getOpposingJouster().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getOpposingJouster().getEntityId());
            g.writeFieldName("rarity");
            g.writeString(value.getOpposingJouster().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("friendly");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getFriendly().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getFriendly().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("opposing");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getOpposing().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getOpposing().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("winner");
        g.writeBoolean(value.isWinner());

        g.writeEndObject();

    }
}
