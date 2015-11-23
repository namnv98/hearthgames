package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.parse.domain.Zone;

public class CardDiscarded implements Action {

    private String causeSide;
    private String cardSide;
    private Zone fromZone;
    private Zone toZone;
    private Player player;
    private Card card;
    private Card cause;

    public CardDiscarded(String causeSide, String cardSide, Zone fromZone, Zone toZone, Player player, Card card, Card cause) {
        this.causeSide = causeSide;
        this.cardSide = cardSide;
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.player = player;
        this.card = card;
        this.cause = cause;
    }

    public Zone getFromZone() {
        return fromZone;
    }

    public void setFromZone(Zone fromZone) {
        this.fromZone = fromZone;
    }

    public Zone getToZone() {
        return toZone;
    }

    public void setToZone(Zone toZone) {
        this.toZone = toZone;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCause() {
        return cause;
    }

    public String getCauseSide() {
        return causeSide;
    }

    public void setCauseSide(String causeSide) {
        this.causeSide = causeSide;
    }

    public String getCardSide() {
        return cardSide;
    }

    public void setCardSide(String cardSide) {
        this.cardSide = cardSide;
    }

    public void setCause(Card cause) {
        this.cause = cause;
    }

    @Override
    public int getType() {
        return 25;
    }
}
