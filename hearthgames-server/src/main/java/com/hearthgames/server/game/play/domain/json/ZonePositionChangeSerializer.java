package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.ZonePositionChange;

import java.io.IOException;

public class ZonePositionChangeSerializer extends JsonSerializer<ZonePositionChange> {
    @Override
    public void serialize(ZonePositionChange value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getName());
            g.writeFieldName("cardId");
            g.writeString(value.getCard().getCardDetails().getId());
            g.writeFieldName("id");
            g.writeString(value.getCard().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetails().getRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("zone");
        g.writeString(value.getZone().name());

        g.writeFieldName("position");
        g.writeNumber(value.getPosition());

        g.writeEndObject();
    }
}
