package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.play.domain.json.ManaGainedSerializer;

import java.io.Serializable;

@JsonSerialize(using = ManaGainedSerializer.class)
public class ManaGained implements Action, Serializable {

    private int manaGained;

    public ManaGained(int manaGained) {
        this.manaGained = manaGained;
    }

    public int getManaGained() {
        return manaGained;
    }

    public void setManaGained(int manaGained) {
        this.manaGained = manaGained;
    }

}
