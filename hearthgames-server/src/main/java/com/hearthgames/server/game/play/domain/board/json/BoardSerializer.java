package com.hearthgames.server.game.play.domain.board.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Action;
import com.hearthgames.server.game.play.domain.board.Board;
import com.hearthgames.server.game.play.domain.board.CardInHand;

import java.io.IOException;

public class BoardSerializer extends JsonSerializer<Board> {

    @Override
    public void serialize(Board value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("turnData");
        g.writeObject(value.getTurnData());
        g.writeFieldName("friendlyHero");
        g.writeObject(value.getFriendlyHero());
        g.writeFieldName("opposingHero");
        g.writeObject(value.getOpposingHero());
        g.writeFieldName("actions");
        g.writeStartArray();
        for (Action action: value.getActions()) {
            g.writeObject(action);
        }
        g.writeEndArray();

        g.writeEndObject();
    }
}
