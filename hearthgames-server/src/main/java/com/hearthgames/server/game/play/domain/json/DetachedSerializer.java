package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Detached;

import java.io.IOException;

public class DetachedSerializer extends JsonSerializer<Detached> {
    @Override
    public void serialize(Detached value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("detached");

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

        // as an example we can emperor thaurasian can reduce the mana cost of cards we never know about so we need to guard against not know what the enchantment was detached from
        if (value.getDetachedFrom() != null) {
            g.writeFieldName("detachedFrom");
            g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDetachedFrom().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getDetachedFrom().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getDetachedFrom().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getDetachedFrom().getCardDetailsRarity().toLowerCase());
            g.writeEndObject();
        }

        g.writeEndObject();
    }
}
