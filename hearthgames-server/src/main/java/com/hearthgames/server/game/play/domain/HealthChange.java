package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.HealthChangeSerializer;

import java.io.Serializable;

@JsonSerialize(using = HealthChangeSerializer.class)
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

        return cardController.getName() + " " + card.getCardDetailsName() + " health is now : " + newHealth;
    }
}
