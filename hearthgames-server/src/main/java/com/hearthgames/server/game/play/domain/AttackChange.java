package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.AttackChangeSerializer;

import java.io.Serializable;

@JsonSerialize(using = AttackChangeSerializer.class)
public class AttackChange implements Action, Serializable {

    private Player cardController;
    private Card card;
    private int amount; // amount of increase
    private int newAttack;

    public AttackChange(Player cardController, Card card, int amount, int newAttack) {
        this.cardController = cardController;
        this.card = card;
        this.amount = amount;
        this.newAttack = newAttack;
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

    public int getNewAttack() {
        return newAttack;
    }

    public void setNewAttack(int newAttack) {
        this.newAttack = newAttack;
    }

    @Override
    public String toString() {
        return cardController.getName() + " " + card.getCardDetailsName() + " attack is now : " + newAttack;
    }
}
