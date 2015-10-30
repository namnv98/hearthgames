package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;
import com.hearthlogs.server.match.domain.Player;

public class CardPlayed implements Action {

    private Player beneficiary;
    private Card card;

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
