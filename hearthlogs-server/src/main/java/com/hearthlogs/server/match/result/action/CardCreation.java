package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;
import com.hearthlogs.server.match.domain.Player;

public class CardCreation implements Action {

    private Player beneficiary;
    private Card creator;
    private Card created;

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
