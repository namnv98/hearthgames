package com.hearthgames.server.game.play.domain.board.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hearthgames.server.game.play.domain.board.CardInHand;
import com.hearthgames.server.game.play.domain.board.CardInSecret;
import com.hearthgames.server.game.play.domain.board.Hero;
import com.hearthgames.server.game.play.domain.board.MinionInPlay;

import java.io.IOException;

public class HeroSerializer extends JsonSerializer<Hero> {

    @Override
    public void serialize(Hero value, JsonGenerator g, SerializerProvider serializers) throws IOException {

        g.writeStartObject();

        if (value.getId() != null) {
            g.writeFieldName("id");
            g.writeString(value.getId());
        }
        if (value.getCardId() != null) {
            g.writeFieldName("cardId");
            g.writeString(value.getCardId());
        }
        if (value.getHealth() != null) {
            g.writeFieldName("health");
            g.writeNumber(value.getHealth());
        }
        if (value.getArmor() != null) {
            g.writeFieldName("armor");
            g.writeNumber(value.getArmor());
        }
        if (value.getAttack() != null) {
            g.writeFieldName("attack");
            g.writeNumber(value.getAttack());
        }
        if (value.getMana() != null) {
            g.writeFieldName("mana");
            g.writeNumber(value.getMana());
        }
        if (value.getManaTotal() != null) {
            g.writeFieldName("manaTotal");
            g.writeNumber(value.getManaTotal());
        }
        if (value.getDamaged() != null && value.getDamaged()) {
            g.writeFieldName("damaged");
            g.writeBoolean(value.getDamaged());
        }
        if (value.getPowerId() != null) {
            g.writeFieldName("powerId");
            g.writeString(value.getPowerId());
        }
        if (value.getPowerUsed() != null && value.getPowerUsed()) {
            g.writeFieldName("powerUsed");
            g.writeBoolean(value.getPowerUsed());
        }
        g.writeFieldName("cardsInHand");
        g.writeStartArray();
        for (CardInHand cardInHand: value.getCardsInHand()) {
            g.writeObject(cardInHand);
        }
        g.writeEndArray();
        g.writeFieldName("cardsInSecret");
        g.writeStartArray();
        for (CardInSecret cardInSecret: value.getCardsInSecret()) {
            g.writeObject(cardInSecret);
        }
        g.writeEndArray();
        g.writeFieldName("minionsInPlay");
        g.writeStartArray();
        for (MinionInPlay minionInPlay: value.getMinionsInPlay()) {
            g.writeObject(minionInPlay);
        }
        g.writeEndArray();
        g.writeFieldName("weapon");
        g.writeObject(value.getWeapon());

        g.writeEndObject();
    }
}
