package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Entity;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.CardDrawnSerializer;

import java.io.Serializable;

@JsonSerialize(using = CardDrawnSerializer.class)
public class CardDrawn implements Action, Serializable {

    private Player drawer;
    private Card card;
    private Card trigger;

    public CardDrawn(Player drawer, Card card, Entity trigger) {
        this.drawer = drawer;
        this.card = card;
        if (trigger instanceof Card) {
            this.trigger = (Card) trigger;
        }
    }

    public Player getDrawer() {
        return drawer;
    }

    public void setDrawer(Player drawer) {
        this.drawer = drawer;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getTrigger() {
        return trigger;
    }

    public void setTrigger(Card trigger) {
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return drawer.getName() + " has drawn " + card.getCardDetailsName();
    }
}
