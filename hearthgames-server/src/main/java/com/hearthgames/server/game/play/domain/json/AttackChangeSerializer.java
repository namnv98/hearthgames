package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.AttackChange;

import java.io.IOException;

public class AttackChangeSerializer extends JsonSerializer<AttackChange> {
    @Override
    public void serialize(AttackChange value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("attackChange");

        g.writeFieldName("card");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCard().getCardDetailsName());
            g.writeFieldName("cardId");
            g.writeString(value.getCard().getCardDetailsId());
            g.writeFieldName("id");
            g.writeString(value.getCard().getEntityId());
            g.writeFieldName("rarity");
            g.writeString(value.getCard().getCardDetailsRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("cardController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getCardController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getCardController().getPlayerClassToLowerCase());
        g.writeEndObject();

        g.writeFieldName("amount");
        g.writeNumber(value.getAmount());

        g.writeFieldName("attack");
        g.writeNumber(value.getNewAttack());

        g.writeEndObject();
    }
}
