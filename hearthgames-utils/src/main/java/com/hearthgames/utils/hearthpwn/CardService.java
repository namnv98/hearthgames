package com.hearthgames.utils.hearthpwn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CardService {

    private Map<String, CardDetails> cards = new HashMap<>();

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
        for (CardDetails c: cardSets.getHeroSkins()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getTheGrandTournament()) {
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getLeagueOfExplorers()) {
            cards.put(c.getId(), c);
        }
    }

    public CardDetails getCardDetails(String id) {
        return cards.get(id);
    }

    public String getName(String id) {
        return cards.get(id).getName();
    }

    public List<CardDetails> getCardDetailsByName(String name) {
        return cards.values().stream().filter(cardDetails -> cardDetails.getName().equals(name)).collect(Collectors.toList());
    }

    public Collection<CardDetails> getCardDetails() {
        return cards.values();
    }

}
