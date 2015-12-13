package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Entity;
import com.hearthgames.server.game.play.domain.json.ManaUsedSerializer;

import java.io.Serializable;

@JsonSerialize(using = ManaUsedSerializer.class)
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
