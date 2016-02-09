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
            g.writeString(value.getAttacker().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getAttacker().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getAttacker().getEntityId());
            g.writeFieldName("rarity");
            g.writeString(value.getAttacker().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("attackerController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getAttackerController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getAttackerController().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("defender");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDefender().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getDefender().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getDefender().getEntityId());
            g.writeFieldName("rarity");
            g.writeString(value.getDefender().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("defenderController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getDefenderController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getDefenderController().getPlayerClassToLowerCase());
        g.writeEndObject();

        if (value.getOriginalDefender() != null) {
            g.writeFieldName("originalDefender");
            g.writeStartObject();
                g.writeFieldName("name");
                g.writeString(value.getOriginalDefender().getCardDetailsName());
                g.writeFieldName("cardId");
                g.writeString(value.getOriginalDefender().getCardDetailsId());
                g.writeFieldName("id");
                g.writeString(value.getOriginalDefender().getEntityId());
                g.writeFieldName("rarity");
                g.writeString(value.getOriginalDefender().getCardDetailsRarity().toLowerCase());
            g.writeEndObject();

            g.writeFieldName("originalDefenderController");
            g.writeStartObject();
                g.writeFieldName("name");
                g.writeString(value.getOriginalDefenderController().getName());
                g.writeFieldName("playerClass");
                g.writeString(value.getOriginalDefenderController().getPlayerClassToLowerCase());
            g.writeEndObject();
        }

        g.writeEndObject();

    }
}
