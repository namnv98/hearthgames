package com.hearthgames.server.game.play.domain.board;

public class MinionInPlay extends CardIn {

    private String id;
    private boolean frozen;
    private boolean silenced;
    private boolean stealthed;
    private boolean taunting;
    private boolean shielded;
    private boolean legendary;
    private String icon; // i.e. Inspire, Trigger, Deathrattle, etc...
    private int attack;
    private boolean attackBuffed;
    private int health;
    private boolean healthBuffed;
    private boolean damaged;
    private boolean exhausted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isSilenced() {
        return silenced;
    }

    public void setSilenced(boolean silenced) {
        this.silenced = silenced;
    }

    public boolean isStealthed() {
        return stealthed;
    }

    public void setStealthed(boolean stealthed) {
        this.stealthed = stealthed;
    }

    public boolean isTaunting() {
        return taunting;
    }

    public void setTaunting(boolean taunting) {
        this.taunting = taunting;
    }

    public boolean isShielded() {
        return shielded;
    }

    public void setShielded(boolean shielded) {
        this.shielded = shielded;
    }

    public boolean isLegendary() {
        return legendary;
    }

    public void setLegendary(boolean legendary) {
        this.legendary = legendary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public boolean isExhausted() {
        return exhausted;
    }

    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
    }

    public boolean isAttackBuffed() {
        return attackBuffed;
    }

    public void setAttackBuffed(boolean attackBuffed) {
        this.attackBuffed = attackBuffed;
    }

    public boolean isHealthBuffed() {
        return healthBuffed;
    }

    public void setHealthBuffed(boolean healthBuffed) {
        this.healthBuffed = healthBuffed;
    }
}
