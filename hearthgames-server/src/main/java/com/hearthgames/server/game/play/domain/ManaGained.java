package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.ManaGainedSerializer;

import java.io.Serializable;

@JsonSerialize(using = ManaGainedSerializer.class)
public class ManaGained implements Action, Serializable {

    private Player player;
    private int manaGained;

    public ManaGained(Player player, int manaGained) {
        this.player = player;
        this.manaGained = manaGained;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getManaGained() {
        return manaGained;
    }

    public void setManaGained(int manaGained) {
        this.manaGained = manaGained;
    }

}
