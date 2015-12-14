package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.domain.json.CardPlayedSerializer;

import java.io.Serializable;

@JsonSerialize(using = CardPlayedSerializer.class)
public class CardPlayed implements Action, Serializable {

    private Zone fromZone;
    private Zone toZone;
    private Player player;
    private Card card;
    private int playType;

    public CardPlayed(Zone fromZone, Zone toZone, Player player, Card card) {
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.player = player;
        this.card = card;

        if (Zone.HAND == fromZone && Zone.PLAY == toZone && card.isMinion()) {
            this.playType = 1;
        } else if (Zone.HAND == fromZone && Zone.SECRET == toZone && card.isSpell()) {
            this.playType = 2;
        } else if (Zone.DECK == fromZone && Zone.PLAY == toZone && card.isMinion()) {
            this.playType = 3;
        } else if (Zone.DECK == fromZone && Zone.SECRET == toZone && card.isSpell()) {
            this.playType = 4;
        } else if (Zone.GRAVEYARD == fromZone && Zone.PLAY == toZone && card.isMinion()) {
            this.playType = 5;
        } else if (Zone.PLAY == fromZone && Zone.PLAY == toZone && card.isMinion()) {
            this.playType = 6;
        } else if (Zone.HAND == fromZone && Zone.PLAY == toZone && card.isSpell()) {
            this.playType = 7;
        }
    }

    public int getPlayType() {
        return playType;
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
        if (playType == 1) {
            return player.getName() + " has played a minion from HAND : " + card.getName();
        } else if (playType == 2) {
            return player.getName() + " has played a secret from HAND : " + card.getName();
        } else if (playType == 3) {
            return player.getName() + " has played a minion from DECK : " + card.getName();
        } else if (playType == 4) {
            return player.getName() + " has played a secret from DECK : " + card.getName();
        } else if (playType == 5) {
            return player.getName() + " has played a minion from GRAVEYARD : " + card.getName();
        } else if (playType == 6) {
            return player.getName() + " has gained a minion : " + card.getName();
        } else if (playType == 7) {
            return player.getName() + " has cast a spell : " + card.getName();
        }
        return "";
    }
}
