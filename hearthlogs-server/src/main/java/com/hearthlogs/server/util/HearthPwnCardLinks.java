package com.hearthlogs.server.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HearthPwnCardLinks implements Serializable {

    private static final long serialVersionUID = 1;

    private List<HearthPwnCardLink> cards = new ArrayList<>();

    public List<HearthPwnCardLink> getCards() {
        return cards;
    }

    public void setCards(List<HearthPwnCardLink> cards) {
        this.cards = cards;
    }
}
