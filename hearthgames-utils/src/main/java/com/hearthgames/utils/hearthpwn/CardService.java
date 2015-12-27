package com.hearthgames.utils.hearthpwn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardService {

    private CardCollection cardCollection;

    @Autowired
    public CardService(CardCollection cardCollection) {
        this.cardCollection = cardCollection;
    }

    public CardDetails getCardDetails(String id) {
        return cardCollection.get(id);
    }

    public String getName(String id) {
        return cardCollection.get(id).getName();
    }

    public List<CardDetails> getCardDetailsByName(String name) {
        return cardCollection.getCards().values().stream().filter(cardDetails -> cardDetails.getName().equals(name)).collect(Collectors.toList());
    }

    public Collection<CardDetails> getCardDetails() {
        return cardCollection.getCards().values();
    }

}
