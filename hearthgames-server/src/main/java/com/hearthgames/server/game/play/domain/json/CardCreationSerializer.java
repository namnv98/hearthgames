package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.CardCreation;

import java.io.IOException;

public class CardCreationSerializer extends JsonSerializer<CardCreation> {
    @Override
    public void serialize(CardCreation value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("creator");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCreator().getName());
            g.writeFieldName("id");
            g.writeString(value.getCreator().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCreator().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("created");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCreated().getName());
            g.writeFieldName("id");
            g.writeString(value.getCreated().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCreated().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("creatorPlayer");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCreatorController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getCreatorController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("createdPlayer");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCreatedController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getCreatedController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("beneficiary");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getBeneficiary().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getBeneficiary().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeEndObject();
    }
}
