package com.hearthlogs.server.game.parse.domain;

public class CardWrapper {

    private int realHealth;
    private int realAttack;

    private Card card;

    public CardWrapper(Card card) {
        this.card = card;
        int health = card.getHealth() != null ? Integer.parseInt(card.getHealth()) : 0;
        int damage = card.getDamage() != null ? Integer.parseInt(card.getDamage()) : 0;
        realHealth = health - damage;
        realAttack = card.getAtk() != null ? Integer.parseInt(card.getAtk()) : 0;
    }

    public int getRealHealth() {
        return realHealth;
    }

    public void setRealHealth(int realHealth) {
        this.realHealth = realHealth;
    }

    public int getRealAttack() {
        return realAttack;
    }

    public void setRealAttack(int realAttack) {
        this.realAttack = realAttack;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
