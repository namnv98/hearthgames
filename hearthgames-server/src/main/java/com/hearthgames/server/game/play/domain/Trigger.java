package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.domain.json.TriggerSerializer;

import java.io.Serializable;

@JsonSerialize(using = TriggerSerializer.class)
public class Trigger implements Action, Serializable {

    private Card card;

    public Trigger(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}
