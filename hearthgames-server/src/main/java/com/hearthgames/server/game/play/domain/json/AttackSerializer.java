package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Attack;

import java.io.IOException;

public class AttackSerializer extends JsonSerializer<Attack> {

    @Override
    public void serialize(Attack value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("attack");

        g.writeFieldName("attacker");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getAttacker().getName());
            g.writeFieldName("cardId");
            g.writeString(value.getAttacker().getCardDetails().getId());
            g.writeFieldName("id");
            g.writeString(value.getAttacker().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getAttacker().getCardDetails().getRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("attackerController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getAttackerController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getAttackerController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("defender");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDefender().getName());
            g.writeFieldName("cardId");
            g.writeString(value.getDefender().getCardDetails().getId());
            g.writeFieldName("id");
            g.writeString(value.getDefender().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getDefender().getCardDetails().getRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("defenderController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDefenderController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getDefenderController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeEndObject();

    }
}
