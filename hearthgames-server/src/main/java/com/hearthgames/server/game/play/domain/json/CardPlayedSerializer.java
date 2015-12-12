package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.CardPlayed;

import java.io.IOException;

public class CardPlayedSerializer extends JsonSerializer<CardPlayed> {
    @Override
    public void serialize(CardPlayed value, JsonGenerator g, SerializerProvider serializers) throws IOException {

    }
}
