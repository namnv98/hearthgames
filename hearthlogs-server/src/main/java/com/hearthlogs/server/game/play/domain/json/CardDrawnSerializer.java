package com.hearthlogs.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthlogs.server.game.play.domain.CardDrawn;

import java.io.IOException;

public class CardDrawnSerializer extends JsonSerializer<CardDrawn> {
    @Override
    public void serialize(CardDrawn value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
