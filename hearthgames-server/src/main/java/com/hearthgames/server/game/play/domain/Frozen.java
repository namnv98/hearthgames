package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.FrozenSerializer;

import java.io.Serializable;

@JsonSerialize(using = FrozenSerializer.class)
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
        return cardController.getName() + " " + card.getCardDetailsName() + " has been frozen";
    }
}
