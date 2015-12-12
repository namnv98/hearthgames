package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.CardDiscarded;

import java.io.IOException;

public class CardDiscardedSerializer extends JsonSerializer<CardDiscarded> {
    @Override
    public void serialize(CardDiscarded value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
