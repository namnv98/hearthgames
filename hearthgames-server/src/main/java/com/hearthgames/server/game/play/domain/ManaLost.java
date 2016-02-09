package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.domain.json.ManaLostSerializer;

import java.io.Serializable;

@JsonSerialize(using = ManaLostSerializer.class)
public class ManaLost implements Action, Serializable {

    private Card card;
    private int amount;

    public ManaLost(Card card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}
