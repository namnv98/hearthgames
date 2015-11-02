package com.hearthlogs.server.match.play.domain;

import java.io.Serializable;

public class ManaGained implements Action, Serializable {

    private int manaGained;

    protected ManaGained(int manaGained) {
        this.manaGained = manaGained;
    }

    public int getManaGained() {
        return manaGained;
    }

    public void setManaGained(int manaGained) {
        this.manaGained = manaGained;
    }
}
