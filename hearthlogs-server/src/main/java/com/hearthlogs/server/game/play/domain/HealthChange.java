package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class HealthChange implements Action, Serializable {

    private String side;
    private Card card;
    private int amount; // amount increased or decreased
    private int newHealth;

    public HealthChange(String side, Card card, int amount, int newHealth) {
        this.side = side;
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

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getNewHealth() {
        return newHealth;
    }

    public void setNewHealth(int newHealth) {
        this.newHealth = newHealth;
    }

    @Override
    public int getType() {
        return 12;
    }
}
