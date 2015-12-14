package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.TempManaGainedSerializer;

import java.io.Serializable;

@JsonSerialize(using = TempManaGainedSerializer.class)
public class TempManaGained implements Action, Serializable {

    private Player player;
    private Card card;
    private int tempManaGained;

    public TempManaGained(Player player, Card card, int tempManaGained) {
        this.player = player;
        this.card = card;
        this.tempManaGained = tempManaGained;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getTempManaGained() {
        return tempManaGained;
    }

    public void setTempManaGained(int tempManaGained) {
        this.tempManaGained = tempManaGained;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}

