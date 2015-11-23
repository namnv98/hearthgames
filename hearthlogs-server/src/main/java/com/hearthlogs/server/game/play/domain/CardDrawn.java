package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Entity;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class CardDrawn implements Action, Serializable {

    private Player beneficiary;
    private Card card;
    private Entity trigger;

    public CardDrawn(Player beneficiary, Card card, Entity trigger) {
        this.beneficiary = beneficiary;
        this.card = card;
        this.trigger = trigger;
    }

    public Player getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Player beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Entity getTrigger() {
        return trigger;
    }

    public void setTrigger(Entity trigger) {
        this.trigger = trigger;
    }

    @Override
    public int getType() {
        return 6;
    }
}
