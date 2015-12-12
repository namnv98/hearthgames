package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.ArmorChangeSerializer;

import java.io.Serializable;

@JsonSerialize(using = ArmorChangeSerializer.class)
public class ArmorChange implements Action, Serializable {

    private Player player;
    private Card card;
    private int armor;

    public ArmorChange(Player player, Card card, int armor) {
        this.player = player;
        this.card = card;
        this.armor = armor;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return player.getName() + " " + card.getName() + " armor is now : " + armor;
    }
}