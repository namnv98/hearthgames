package com.hearthgames.server.game.play.domain.board.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.board.Weapon;

import java.io.IOException;

public class WeaponSerializer extends JsonSerializer<Weapon> {

    @Override
    public void serialize(Weapon value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("id");
        g.writeString(value.getId());
        g.writeFieldName("cardId");
        g.writeString(value.getCardId());
        g.writeFieldName("attack");
        g.writeNumber(value.getAttack());
        g.writeFieldName("durability");
        g.writeNumber(value.getDurability());

        g.writeEndObject();
    }
}
