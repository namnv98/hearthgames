package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Entity;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.ManaUsedSerializer;

import java.io.Serializable;

@JsonSerialize(using = ManaUsedSerializer.class)
public class ManaUsed implements Action, Serializable {

    private Player player;
    private Entity entity;
    private int manaUsed;

    public ManaUsed(Player player, Entity card, int manaUsed) {
        this.player = player;
        this.entity = card;
        this.manaUsed = manaUsed;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
