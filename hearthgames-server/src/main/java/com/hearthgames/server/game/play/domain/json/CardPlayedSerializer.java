package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.CardPlayed;

import java.io.IOException;

public class CardPlayedSerializer extends JsonSerializer<CardPlayed> {
    @Override
    public void serialize(CardPlayed value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("cardPlayed");

        g.writeFieldName("playType"+value.getPlayType());
        g.writeBoolean(true);

        g.writeFieldName("from");
        g.writeString(value.getFromZone().name());

        g.writeFieldName("to");
        g.writeString(value.getToZone().name());

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getCard().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getCard().getEntityId());
        g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("player");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getPlayer().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getPlayer().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeEndObject();
    }
}
