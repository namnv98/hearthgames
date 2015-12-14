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
            g.writeString(value.getCard().getName());
            g.writeFieldName("id");
            g.writeString(value.getCard().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetails().getRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("attachedTo");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getAttachedTo().getName());
            g.writeFieldName("id");
            g.writeString(value.getAttachedTo().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getAttachedTo().getCardDetails().getRarity().toLowerCase());
        g.writeEndObject();

        g.writeEndObject();
    }
}
