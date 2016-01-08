package com.hearthgames.server.game.play.domain.board.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.board.CardInSecret;

import java.io.IOException;

public class CardInSecretSerializer extends JsonSerializer<CardInSecret> {

    @Override
    public void serialize(CardInSecret value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("id");
        g.writeString(value.getId());
        g.writeFieldName("cardId");
        g.writeString(value.getCardId());
        g.writeFieldName("cardClass");
        g.writeString(value.getCardClass());

        g.writeEndObject();
    }
}