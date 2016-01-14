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

        g.writeFieldName("type");
        g.writeString("cardCreation");

        if (value.getCreator() != null) {
            g.writeFieldName("creator");
            g.writeStartObject();
                g.writeFieldName("name");
                g.writeString(value.getCreator().getCardDetailsName());
                g.writeFieldName("cardId");
                g.writeString(value.getCreator().getCardDetailsId());
                g.writeFieldName("id");
                g.writeString(value.getCreator().getId());
                g.writeFieldName("rarity");
                g.writeString(value.getCreator().getCardDetailsRarity().toLowerCase());
            g.writeEndObject();
        } else {
            g.writeFieldName("creator");
            g.writeStartObject();
                g.writeStringField("name", "Game");
            g.writeEndObject();
        }

        g.writeFieldName("created");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCreated().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getCreated().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getCreated().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCreated().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        if (value.getCreatorController() != null) {
            g.writeFieldName("creatorController");
                g.writeStartObject();
                g.writeFieldName("name");
                g.writeString(value.getCreatorController().getName());
                g.writeFieldName("playerClass");
                g.writeString(value.getCreatorController().getPlayerClassToLowerCase());
            g.writeEndObject();
        }

        if (value.getCreatedController() != null) {
            g.writeFieldName("createdController");
                g.writeStartObject();
                g.writeFieldName("name");
                g.writeString(value.getCreatedController().getName());
                g.writeFieldName("playerClass");
                g.writeString(value.getCreatedController().getPlayerClassToLowerCase());
            g.writeEndObject();
        }

        if (value.getBeneficiary() != null) {
            g.writeFieldName("beneficiary");
                g.writeStartObject();
                g.writeFieldName("name");
                g.writeString(value.getBeneficiary().getName());
                g.writeFieldName("playerClass");
                g.writeString(value.getBeneficiary().getPlayerClassToLowerCase());
            g.writeEndObject();
        }

        g.writeEndObject();
    }
}
