package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.HeroHealthChangeSerializer;

import java.io.Serializable;

@JsonSerialize(using = HeroHealthChangeSerializer.class)
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
        return cardController.getName() + " " + card.getCardDetailsName() + " hero health is now : " + health;
    }
}
