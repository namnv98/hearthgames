package com.hearthlogs.web.service;

import com.hearthlogs.web.domain.CardSets;
import com.hearthlogs.web.domain.CardDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CardService {

    private HashMap<String, CardDetails> cards = new HashMap<>();

    @Autowired
    public CardService(CardSets cardSets) {
        for (CardDetails c: cardSets.getBasic()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getBlackrockMountain()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getClassic()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getCurseOfNaxxramas()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getGoblinsVsGnomes()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getMissions()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getPromotion()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getReward()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getTavernBrawl()) {
            cards.put(c.getId(), c);
        }
    }

    public CardDetails getCardDetails(String id) {
        return cards.get(id);
    }

    public String getName(String id) {
        return cards.get(id).getName();
    }
}
