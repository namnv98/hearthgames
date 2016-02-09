package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Damage;

import java.io.IOException;

public class DamageSerializer extends JsonSerializer<Damage> {
    @Override
    public void serialize(Damage value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("damage");

        g.writeFieldName("damager");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDamager().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getDamager().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getDamager().getEntityId());
            g.writeFieldName("rarity");
            g.writeString(value.getDamager().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("damaged");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDamaged().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getDamaged().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getDamaged().getEntityId());
            g.writeFieldName("rarity");
            g.writeString(value.getDamaged().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        if (value.getDamaged().isWeapon()) {
            g.writeFieldName("weapon");
            g.writeBoolean(true);
        }

        g.writeFieldName("damagerController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDamagerController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getDamagerController().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("damagedController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDamagedController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getDamagedController().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("amount");
        g.writeNumber(value.getAmount());

        g.writeEndObject();
    }
}
