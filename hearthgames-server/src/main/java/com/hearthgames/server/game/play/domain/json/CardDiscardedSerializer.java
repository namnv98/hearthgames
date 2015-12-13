package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.CardDiscarded;

import java.io.IOException;

public class CardDiscardedSerializer extends JsonSerializer<CardDiscarded> {
    @Override
    public void serialize(CardDiscarded value, JsonGenerator g, SerializerProvider serializers) throws IOException {

        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("cardDiscarded");

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getName());
            g.writeFieldName("id");
            g.writeString(value.getCard().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("cause");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCause().getName());
            g.writeFieldName("id");
            g.writeString(value.getCause().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCause().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("cardController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCardController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getCardController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("causeController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCauseController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getCauseController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("player");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getPlayer().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getPlayer().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeEndObject();
    }
}