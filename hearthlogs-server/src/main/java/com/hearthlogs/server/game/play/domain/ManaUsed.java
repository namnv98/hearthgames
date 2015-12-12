package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Entity;

import java.io.Serializable;

public class ManaUsed implements Action, Serializable {

    private Entity entity;
    private int manaUsed;

    public ManaUsed(Entity card, int manaUsed) {
        this.entity = card;
        this.manaUsed = manaUsed;
    }

    public int getManaUsed() {
        return manaUsed;
    }

    public void setManaUsed(int manaUsed) {
        this.manaUsed = manaUsed;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}
