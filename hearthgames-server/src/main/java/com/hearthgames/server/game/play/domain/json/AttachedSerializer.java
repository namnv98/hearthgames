package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Attached;

import java.io.IOException;

public class AttachedSerializer extends JsonSerializer<Attached> {
    @Override
    public void serialize(Attached value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("attached");

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getCard().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getCard().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("attachedTo");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getAttachedTo().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getAttachedTo().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getAttachedTo().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getAttachedTo().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeEndObject();
    }
}
