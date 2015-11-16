package com.hearthlogs.server.service;

import com.hearthlogs.server.match.parse.domain.CardDetails;
import com.hearthlogs.server.match.parse.domain.CardSets;
import com.hearthlogs.server.util.HearthPwnCardLink;
import com.hearthlogs.server.util.HearthPwnCardLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CardService {

    private Map<String, CardDetails> cards = new HashMap<>();

    @Autowired
    public CardService(CardSets cardSets, HearthPwnCardLinks cardLinks) {

        Map<String, HearthPwnCardLink> cardLinkMap = new HashMap<>();
        for (HearthPwnCardLink cardLink: cardLinks.getCards()) {
            cardLinkMap.put(cardLink.getCardId(), cardLink);
        }

        for (CardDetails c: cardSets.getBasic()) {
            c.setCardLink(cardLinkMap.get(c.getId()));
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getBlackrockMountain()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getClassic()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getCurseOfNaxxramas()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getGoblinsVsGnomes()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getMissions()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getPromotion()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getReward()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getTavernBrawl()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getHeroSkins()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getTheGrandTournament()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
            cards.put(c.getId(), c);
        }
        for (CardDetails c: cardSets.getLeagueOfExplorers()) {
            c.setCardLink(cardLinkMap.get(c.getId())); 
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

}
