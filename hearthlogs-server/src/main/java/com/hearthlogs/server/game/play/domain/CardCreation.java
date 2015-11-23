package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class CardCreation implements Action, Serializable {

    private String creatorSide;
    private String createdSide;
    private Player beneficiary;
    private Card creator;
    private Card created;

    public CardCreation(String creatorSide, String createdSide, Player beneficiary, Card creator, Card created) {
        this.creatorSide = creatorSide;
        this.createdSide = createdSide;
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

    public String getCreatorSide() {
        return creatorSide;
    }

    public void setCreatorSide(String creatorSide) {
        this.creatorSide = creatorSide;
    }

    public String getCreatedSide() {
        return createdSide;
    }

    public void setCreatedSide(String createdSide) {
        this.createdSide = createdSide;
    }

    @Override
    public int getType() {
        return 5;
    }
}
