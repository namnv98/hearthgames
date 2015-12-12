package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

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
        return cardController.getName() + " has activated their hero power : " + card.getCardDetails().getName();

    }
}
