package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.analysis.domain.CardInfo;
import com.hearthlogs.server.game.analysis.domain.generic.GenericColumn;
import com.hearthlogs.server.game.analysis.domain.generic.GenericRow;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CardInfoAnalyzer implements Analyzer<CardInfo> {

    @Override
    public CardInfo analyze(GameResult result, GameContext context) {

        CardInfo info = new CardInfo();

        GenericRow header = new GenericRow();
        header.addColumn(new GenericColumn(""));
        header.addColumn(new GenericColumn("Starting Hand"));
        header.addColumn(new GenericColumn("Mulliganed Cards"));
        header.addColumn(new GenericColumn("Cards In Deck"));
        info.setHeader(header);

        GenericRow friendly = new GenericRow();
        friendly.addColumn(new GenericColumn(context.getFriendlyPlayer().getName()));
        friendly.addColumn(new GenericColumn<>(result.getFriendlyStartingCards()));
        friendly.addColumn(new GenericColumn<>(result.getFriendlyMulliganedCards()));
        friendly.addColumn(new GenericColumn<>(getStartingDeck(context, context.getFriendlyPlayer().getController())));
        info.setFriendly(friendly);

        GenericRow opposing = new GenericRow();
        opposing.addColumn(new GenericColumn(context.getOpposingPlayer().getName()));
        opposing.addColumn(new GenericColumn<>(result.getOpposingStartingCards()));
        opposing.addColumn(new GenericColumn<>(result.getOpposingMulliganedCards()));
        opposing.addColumn(new GenericColumn<>(getStartingDeck(context, context.getOpposingPlayer().getController())));
        info.setOpposing(opposing);

        return info;
    }

    private Set<Card> getStartingDeck(GameContext context, String controller) {
        Set<Card> cards = new HashSet<>();
        List<String> ids = context.getStartingCardIds();
        cards.addAll(context.getCards().stream()
                .filter(card -> Objects.equals(card.getController(), controller) && ids.contains(card.getEntityId()) &&
                                !Card.Type.HERO.eq(card.getCardtype()) && !Card.Type.HERO_POWER.eq(card.getCardtype()) &&
                                !Objects.equals(card.getCardid(), "GAME_005")
                ).collect(Collectors.toList()));
        return cards;
    }

}