package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Damage;

import java.io.IOException;

public class DamageSerializer extends JsonSerializer<Damage> {
    @Override
    public void serialize(Damage value, JsonGenerator g, SerializerProvider serializers) throws IOException, JsonProcessingException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("damage");

        g.writeFieldName("damager");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDamager().getName());
            g.writeFieldName("id");
            g.writeString(value.getDamager().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getDamager().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("damaged");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDamaged().getName());
            g.writeFieldName("id");
            g.writeString(value.getDamaged().getCardDetails().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getDamaged().getCardDetails().getRarity());
        g.writeEndObject();

        g.writeFieldName("damagerController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDamagerController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getDamagerController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("damagedController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDamagedController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getDamagedController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("amount");
        g.writeNumber(value.getAmount());

        g.writeEndObject();
    }
}
