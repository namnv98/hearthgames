package com.hearthlogs.server.game.play.domain.board;

public class MinionInPlay {

    private String id;
    private boolean frozen;
    private boolean taunting;
    private boolean shielded;
    private boolean legendary;
    private String icon; // i.e. Inspire, Trigger, Deathrattle, etc...
    private int attack;
    private int health;

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
}
