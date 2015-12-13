package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.ArmorChange;

import java.io.IOException;

public class ArmorChangeSerializer extends JsonSerializer<ArmorChange> {

    @Override
    public void serialize(ArmorChange value, JsonGenerator g, SerializerProvider serializerProvider) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("armorChange");

        g.writeFieldName("player");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getPlayer().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getPlayer().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getName());
            g.writeFieldName("id");
            g.writeString(value.getCard().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("armor");
        g.writeNumber(value.getArmor());

        g.writeEndObject();
    }
}