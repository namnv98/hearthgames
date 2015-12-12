package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.parse.domain.Zone;

import java.io.Serializable;

public class CardPlayed implements Action, Serializable {

    private Zone fromZone;
    private Zone toZone;
    private Player player;
    private Card card;

    public CardPlayed(Zone fromZone, Zone toZone, Player player, Card card) {
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.player = player;
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    @Override
    public String toString() {
        if (Zone.HAND == fromZone && Zone.PLAY == toZone && card.isMinion()) {
            return player.getName() + " has played a minion from HAND : " + card.getName();
        } else if (Zone.HAND == fromZone && Zone.SECRET == toZone && card.isSpell()) {
            return player.getName() + " has played a secret from HAND : " + card.getName();
        } else if (Zone.DECK == fromZone && Zone.PLAY == toZone && card.isMinion()) {
            return player.getName() + " has played a minion from DECK : " + card.getName();
        } else if (Zone.DECK == fromZone && Zone.SECRET == toZone && card.isSpell()) {
            return player.getName() + " has played a secret from DECK : " + card.getName();
        } else if (Zone.GRAVEYARD == fromZone && Zone.PLAY == toZone && card.isMinion()) {
            return player.getName() + " has played a minion from GRAVEYARD : " + card.getName();
        } else if (Zone.PLAY == fromZone && Zone.PLAY == toZone && card.isMinion()) {
            return player.getName() + " has gained a minion : " + card.getName();
        } else if (Zone.HAND == fromZone && Zone.PLAY == toZone && card.isSpell()) {
            return player.getName() + " has cast a spell : " + card.getName();
        }
        return "";
    }
}
