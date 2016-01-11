package com.hearthgames.server.game.play.domain.board;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.play.domain.board.json.MinionInPlaySerializer;

@JsonSerialize(using = MinionInPlaySerializer.class)
public class MinionInPlay extends CardIn {

    private String cardId;
    private String id;
    private Boolean frozen;
    private Boolean silenced;
    private Boolean stealthed;
    private Boolean taunting;
    private Boolean shielded;
    private Boolean legendary;
    private String icon; // i.e. Inspire, Trigger, Deathrattle, etc...
    private Integer attack;
    private Boolean attackBuffed;
    private Integer health;
    private Boolean healthBuffed;
    private Boolean damaged;
    private Boolean exhausted;
    private Boolean poisonous;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setAttackBuffed(Boolean attackBuffed) {
        this.attackBuffed = attackBuffed;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public Boolean getSilenced() {
        return silenced;
    }

    public void setSilenced(Boolean silenced) {
        this.silenced = silenced;
    }

    public Boolean getStealthed() {
        return stealthed;
    }

    public void setStealthed(Boolean stealthed) {
        this.stealthed = stealthed;
    }

    public Boolean getTaunting() {
        return taunting;
    }

    public void setTaunting(Boolean taunting) {
        this.taunting = taunting;
    }

    public Boolean getShielded() {
        return shielded;
    }

    public void setShielded(Boolean shielded) {
        this.shielded = shielded;
    }

    public Boolean getLegendary() {
        return legendary;
    }

    public void setLegendary(Boolean legendary) {
        this.legendary = legendary;
    }

    public Boolean getAttackBuffed() {
        return attackBuffed;
    }

    public Boolean getHealthBuffed() {
        return healthBuffed;
    }

    public void setHealthBuffed(Boolean healthBuffed) {
        this.healthBuffed = healthBuffed;
    }

    public Boolean getDamaged() {
        return damaged;
    }

    public void setDamaged(Boolean damaged) {
        this.damaged = damaged;
    }

    public Boolean getExhausted() {
        return exhausted;
    }

    public void setExhausted(Boolean exhausted) {
        this.exhausted = exhausted;
    }

    public Boolean getPoisonous() {
        return poisonous;
    }

    public void setPoisonous(Boolean poisonous) {
        this.poisonous = poisonous;
    }
}
