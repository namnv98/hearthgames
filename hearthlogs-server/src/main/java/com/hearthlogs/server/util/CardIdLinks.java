package com.hearthlogs.server.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardIdLinks implements Serializable {

    private static final long serialVersionUID = 1;

    private List<CardIdLink> cards = new ArrayList<>();

    public List<CardIdLink> getCards() {
        return cards;
    }

    public void setCards(List<CardIdLink> cards) {
        this.cards = cards;
    }
}
