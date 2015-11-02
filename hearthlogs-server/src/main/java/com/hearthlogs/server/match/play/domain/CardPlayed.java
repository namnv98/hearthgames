package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Player;

import java.io.Serializable;

public class CardPlayed implements Action, Serializable {

    private Player beneficiary;
    private Card card;

    protected CardPlayed(Player beneficiary, Card card) {
        this.beneficiary = beneficiary;
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Player getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Player beneficiary) {
        this.beneficiary = beneficiary;
    }
}
