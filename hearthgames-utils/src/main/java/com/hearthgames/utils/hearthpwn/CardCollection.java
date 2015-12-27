package com.hearthgames.utils.hearthpwn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardCollection implements Serializable {

    private static final long serialVersionUID = 1;

    private Map<String, CardDetails> cards = new HashMap<>();

    public Map<String, CardDetails> getCards() {
        return cards;
    }

    public void setCards(List<CardDetails> cards) {
        for (CardDetails cardDetails: cards) {
            this.cards.put(cardDetails.getId(), cardDetails);
        }
    }

    public CardDetails get(String id) {
        return this.cards.get(id);
    }
}
