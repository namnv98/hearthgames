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
    private int amount;

    public TempManaGained(Player player, Card card, int amount) {
        this.player = player;
        this.card = card;
        this.amount = amount;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}

