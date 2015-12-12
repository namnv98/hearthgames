package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class HealthChange implements Action, Serializable {

    private Player cardController;
    private Card card;
    private int amount; // amount increased or decreased
    private int newHealth;

    public HealthChange(Player cardController, Card card, int amount, int newHealth) {
        this.cardController = cardController;
        this.card = card;
        this.amount = amount;
        this.newHealth = newHealth;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Player getCardController() {
        return cardController;
    }

    public void setCardController(Player cardController) {
        this.cardController = cardController;
    }

    public int getNewHealth() {
        return newHealth;
    }

    public void setNewHealth(int newHealth) {
        this.newHealth = newHealth;
    }

    @Override
    public String toString() {

        return cardController.getName() + " " + card.getName() + " health is now : " + newHealth;
    }
}
