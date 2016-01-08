package com.hearthgames.server.game.play.domain.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.Kill;

import java.io.IOException;

public class KillSerializer extends JsonSerializer<Kill> {
    @Override
    public void serialize(Kill value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        g.writeStartObject();

        g.writeFieldName("type");
        g.writeString("kill");

        g.writeFieldName("kind");
        g.writeString(value.getKind());

        g.writeFieldName("killer");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getKiller().getName());
            g.writeFieldName("cardId");
            g.writeString(value.getKiller().getCardDetails().getId());
            g.writeFieldName("id");
            g.writeString(value.getKiller().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getKiller().getCardDetails().getRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("killed");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getKilled().getName());
            g.writeFieldName("cardId");
            g.writeString(value.getKilled().getCardDetails().getId());
            g.writeFieldName("id");
            g.writeString(value.getKilled().getId());
            g.writeFieldName("rarity");
            g.writeString(value.getKilled().getCardDetails().getRarity().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("killerController");
            g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getKillerController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getKillerController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("killedController");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getKilledController().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getKilledController().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("beneficiary");
        g.writeStartObject();
            g.writeFieldName("name");
            g.writeString(value.getBeneficiary().getName());
            g.writeFieldName("playerClass");
            g.writeString(value.getBeneficiary().getPlayerClass().toLowerCase());
        g.writeEndObject();

        g.writeFieldName("favorable");
        g.writeBoolean(value.isFavorableTrade());

        g.writeFieldName("even");
        g.writeBoolean(value.isEvenTrade());

        g.writeEndObject();

    }
}
