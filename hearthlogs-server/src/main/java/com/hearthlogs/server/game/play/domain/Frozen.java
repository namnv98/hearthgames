package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class Frozen implements Action, Serializable {

    private Player cardController;
    private Card card;
    private boolean frozen;

    public Frozen(Player cardController, Card card, boolean frozen) {
        this.cardController = cardController;
        this.card = card;
        this.frozen = frozen;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public Player getCardController() {
        return cardController;
    }

    public void setCardController(Player cardController) {
        this.cardController = cardController;
    }

    @Override
    public String toString() {
        return cardController.getName() + " " + card.getName() + " has been frozen";
    }
}
