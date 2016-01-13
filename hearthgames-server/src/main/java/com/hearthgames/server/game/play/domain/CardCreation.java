package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.CardCreationSerializer;

import java.io.Serializable;
@JsonSerialize(using = CardCreationSerializer.class)
public class CardCreation implements Action, Serializable {

    private Player creatorController;
    private Player createdController;
    private Player beneficiary;
    private Card creator;
    private Card created;

    public CardCreation(Player creatorController, Player createdController, Player beneficiary, Card creator, Card created) {
        this.creatorController = creatorController;
        this.createdController = createdController;
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

    public Player getCreatorController() {
        return creatorController;
    }

    public void setCreatorController(Player creatorController) {
        this.creatorController = creatorController;
    }

    public Player getCreatedController() {
        return createdController;
    }

    public void setCreatedController(Player createdController) {
        this.createdController = createdController;
    }

    @Override
    public String toString() {
        return creatorController.getName() + " " + creator.getCardDetailsName() + " has created : " + createdController.getName() + " " +  created.getCardDetailsName() + " ( " + beneficiary.getName() + " has benefited )";
    }
}
