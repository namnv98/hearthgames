package com.hearthgames.server.game.play.domain.board.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.board.MinionInPlay;

import java.io.IOException;

public class MinionInPlaySerializer extends JsonSerializer<MinionInPlay> {
    @Override
    public void serialize(MinionInPlay value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("id");
        g.writeString(value.getId());
        g.writeFieldName("cardId");
        g.writeString(value.getCardId());
        if (value.getIcon() != null) {
            g.writeFieldName("icon");
            g.writeString(value.getIcon());
        }
        if (value.getAttack() != null) {
            g.writeFieldName("attack");
            g.writeNumber(value.getAttack());
        }
        if (value.getHealth() != null) {
            g.writeFieldName("health");
            g.writeNumber(value.getHealth());
        }
        if (value.getFrozen() != null && value.getFrozen()) {
            g.writeFieldName("frozen");
            g.writeBoolean(value.getFrozen());
        }
        if (value.getSilenced() != null && value.getSilenced()) {
            g.writeFieldName("silenced");
            g.writeBoolean(value.getSilenced());
        }
        if (value.getStealthed() != null && value.getStealthed()) {
            g.writeFieldName("stealthed");
            g.writeBoolean(value.getStealthed());
        }
        if (value.getTaunting() != null && value.getTaunting()) {
            g.writeFieldName("taunting");
            g.writeBoolean(value.getTaunting());
        }
        if (value.getShielded() != null && value.getShielded()) {
            g.writeFieldName("shielded");
            g.writeBoolean(value.getShielded());
        }
        if (value.getLegendary() != null && value.getLegendary()) {
            g.writeFieldName("legendary");
            g.writeBoolean(value.getLegendary());
        }
        if (value.getAttackBuffed() != null && value.getAttackBuffed()) {
            g.writeFieldName("attackBuffed");
            g.writeBoolean(value.getAttackBuffed());
        }
        if (value.getHealthBuffed() != null && value.getHealthBuffed()) {
            g.writeFieldName("healthBuffed");
            g.writeBoolean(value.getHealthBuffed());
        }
        if (value.getDamaged() != null && value.getDamaged()) {
            g.writeFieldName("damaged");
            g.writeBoolean(value.getDamaged());
        }
        if (value.getExhausted() != null && value.getExhausted()) {
            g.writeFieldName("exhausted");
            g.writeBoolean(value.getExhausted());
        }
        g.writeEndObject();
    }
}
