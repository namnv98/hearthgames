package com.hearthlogs.server.service;

import com.hearthlogs.server.match.parse.domain.CardDetails;
import com.hearthlogs.server.match.parse.domain.CardSets;
import com.hearthlogs.server.util.HearthPwnCardLink;
import com.hearthlogs.server.util.HearthPwnCards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CardService {

    private Map<String, CardDetails> cards = new HashMap<>();

    private Map<String, CardDetails> cardNames = new HashMap<>();
    private Map<String, CardDetails> tavernBrawlNames = new HashMap<>();

    private Map<String, HearthPwnCardLink> hearthPwnCardLinks = new HashMap<>();

    @Autowired
    public CardService(CardSets cardSets, HearthPwnCards hearthPwnCards) {
        for (CardDetails c: cardSets.getBasic()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getBlackrockMountain()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getClassic()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getCurseOfNaxxramas()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getGoblinsVsGnomes()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getMissions()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getPromotion()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getReward()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getTavernBrawl()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
            tavernBrawlNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getHeroSkins()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getTheGrandTournament()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }
        for (CardDetails c: cardSets.getLeagueOfExplorers()) {
            cards.put(c.getId(), c);
            cardNames.put(c.getName(), c);
        }

        for (HearthPwnCardLink hearthPwnCardLink: hearthPwnCards.getCards()) {
            CardDetails cardDetails = getCardDetails(hearthPwnCardLink.getCardId());
            hearthPwnCardLink.setName(cardDetails.getName());
            hearthPwnCardLinks.put(hearthPwnCardLink.getCardId(), hearthPwnCardLink);
        }
    }

    public CardDetails getCardDetails(String id) {
        return cards.get(id);
    }

    public String getName(String id) {
        return cards.get(id).getName();
    }

    public CardDetails getByName(String name) {
        return cardNames.get(name);
    }

    public boolean isTavernBrawl(String name) {
        return tavernBrawlNames.get(name) != null;
    }

    public HearthPwnCardLink getHearthPwnCardLink(String cardId) {
        return hearthPwnCardLinks.get(cardId);
    }

}
