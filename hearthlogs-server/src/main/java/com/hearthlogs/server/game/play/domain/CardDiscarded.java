package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.parse.domain.Zone;

public class CardDiscarded implements Action {

    private Player causeController;
    private Player cardController;
    private Zone fromZone;
    private Zone toZone;
    private Player player;
    private Card card;
    private Card cause;

    public CardDiscarded(Player causeController, Player cardController, Zone fromZone, Zone toZone, Player player, Card card, Card cause) {
        this.causeController = causeController;
        this.cardController = cardController;
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

    public Player getCauseController() {
        return causeController;
    }

    public void setCauseController(Player causeController) {
        this.causeController = causeController;
    }

    public Player getCardController() {
        return cardController;
    }

    public void setCardController(Player cardController) {
        this.cardController = cardController;
    }

    public void setCause(Card cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return cause.getName() + " has caused " + player.getName() + " to discard " + card.getName();
    }
}
