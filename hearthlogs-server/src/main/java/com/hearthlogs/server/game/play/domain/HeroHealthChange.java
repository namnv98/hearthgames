package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class HeroHealthChange implements Action, Serializable {

    private Player cardController;
    private Card card;
    private int health;

    public HeroHealthChange(Player cardController, Card card, int health) {
        this.cardController = cardController;
        this.card = card;
        this.health = health;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Player getCardController() {
        return cardController;
    }

    public void setCardController(Player cardController) {
        this.cardController = cardController;
    }

    @Override
    public String toString() {
        return cardController.getName() + " " + card.getName() + " hero health is now : " + health;
    }
}
