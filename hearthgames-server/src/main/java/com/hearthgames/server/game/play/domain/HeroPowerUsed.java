package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.HeroPowerUsedSerializer;

import java.io.Serializable;

@JsonSerialize(using = HeroPowerUsedSerializer.class)
public class HeroPowerUsed implements Action, Serializable {

    private Player cardController;
    private Card card;

    public HeroPowerUsed(Player cardController, Card card) {
        this.cardController = cardController;
        this.card = card;
    }

    public Player getCardController() {
        return cardController;
    }

    public void setCardController(Player cardController) {
        this.cardController = cardController;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return cardController.getName() + " has activated their hero power : " + card.getCardDetailsName();

    }
}
