package com.hearthgames.utils.hearthpwn;

import java.io.Serializable;
import java.util.*;

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

    public Map<String, List<CardDetails>> getPlayableCards() {
        Map<String, List<CardDetails>> playableCards = new HashMap<>();
        for (CardDetails cardDetails: cards.values()) {
            boolean excludeSet = "CREDITS".equals(cardDetails.getSet()) || "CHEAT".equals(cardDetails.getSet()) || "NONE".equals(cardDetails.getSet());
            if (!excludeSet) {
                List<CardDetails> setCards = playableCards.get(cardDetails.getSet());
                if (setCards == null) {
                    setCards = new ArrayList<>();
                    playableCards.put(cardDetails.getSet(), setCards);
                }
                setCards.add(cardDetails);
            }
        }
        for (String set: playableCards.keySet()) {
            List<CardDetails> setCards = playableCards.get(set);
            Collections.sort(setCards, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        }
        return playableCards;
    }


    public CardDetails get(String id) {
        return this.cards.get(id);
    }
}
