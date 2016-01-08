package com.hearthgames.server.game.play.domain.board.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.board.CardInHand;

import java.io.IOException;

public class CardInHandSerializer extends JsonSerializer<CardInHand> {

    @Override
    public void serialize(CardInHand value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("cardId");
        g.writeString(value.getCardId());

        g.writeEndObject();
    }
}
