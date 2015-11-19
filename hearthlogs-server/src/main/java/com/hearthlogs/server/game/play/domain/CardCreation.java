package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class CardCreation implements Action, Serializable {

    private Player beneficiary;
    private Card creator;
    private Card created;

    public CardCreation(Player beneficiary, Card creator, Card created) {
        this.beneficiary = beneficiary;
        this.creator = creator;
        this.created = created;
    }

    public Player getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Player beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Card getCreator() {
        return creator;
    }

    public void setCreator(Card creator) {
        this.creator = creator;
    }

    public Card getCreated() {
        return created;
    }

    public void setCreated(Card created) {
        this.created = created;
    }
}