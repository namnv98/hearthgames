package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Entity;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class CardDrawn implements Action, Serializable {

    private Player drawer;
    private Card card;
    private Entity trigger;

    public CardDrawn(Player drawer, Card card, Entity trigger) {
        this.drawer = drawer;
        this.card = card;
        this.trigger = trigger;
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

    public Entity getTrigger() {
        return trigger;
    }

    public void setTrigger(Entity trigger) {
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return drawer.getName() + " has drawn " + card.getName();
    }
}
