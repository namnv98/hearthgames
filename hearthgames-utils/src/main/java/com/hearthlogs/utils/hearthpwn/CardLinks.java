package com.hearthlogs.utils.hearthpwn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardLinks implements Serializable {

    private static final long serialVersionUID = 1;

    private List<CardLink> cards = new ArrayList<>();

    public List<CardLink> getCards() {
        return cards;
    }

    public void setCards(List<CardLink> cards) {
        this.cards = cards;
    }
}
