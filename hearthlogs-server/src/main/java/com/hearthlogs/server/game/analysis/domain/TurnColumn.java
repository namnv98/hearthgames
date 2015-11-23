package com.hearthlogs.server.game.analysis.domain;

import com.hearthlogs.server.game.parse.domain.CardWrapper;
import com.hearthlogs.server.game.play.domain.Action;

import java.util.List;

public class TurnColumn {

    private Action action;
    private String data;
    private List<CardWrapper> cards;

    public TurnColumn(String data) {
        this.data = data;
    }

    public TurnColumn(List<CardWrapper> cards) {
        this.cards = cards;
    }

    public TurnColumn(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<CardWrapper> getCards() {
        return cards;
    }

    public void setCards(List<CardWrapper> cards) {
        this.cards = cards;
    }
}
