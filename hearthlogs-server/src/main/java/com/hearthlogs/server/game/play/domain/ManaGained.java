package com.hearthlogs.server.game.play.domain;

import java.io.Serializable;

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
