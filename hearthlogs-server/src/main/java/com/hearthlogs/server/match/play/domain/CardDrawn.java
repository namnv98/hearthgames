package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Entity;
import com.hearthlogs.server.match.parse.domain.Player;

import java.io.Serializable;

public class CardDrawn implements Action, Serializable {

    private Player beneficiary;
    private Card card;
    private Entity trigger;

    protected CardDrawn(Player beneficiary, Card card, Entity trigger) {
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
}
