package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.ManaUsedSerializer;

import java.io.Serializable;

@JsonSerialize(using = ManaUsedSerializer.class)
public class ManaUsed implements Action, Serializable {

    private Player player;
    private Card entity;
    private int amount;

    public ManaUsed(Player player, Card card, int amount) {
        this.player = player;
        this.entity = card;
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

    public void setAmount(int manaUsed) {
        this.amount = manaUsed;
    }

    public Card getEntity() {
        return entity;
    }

    public void setEntity(Card entity) {
        this.entity = entity;
    }

}
