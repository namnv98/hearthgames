package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class Damage implements Action, Serializable {

    private Player damagerController;
    private Player damagedController;
    private Card damager;
    private Card damaged;
    private int amount;

    public Damage(Player damagerController, Player damagedController, Card damager, Card damaged, int amount) {
        this.damagerController = damagerController;
        this.damagedController = damagedController;
        this.damager = damager;
        this.damaged = damaged;
        this.amount = amount;
    }

    public Card getDamager() {
        return damager;
    }

    public void setDamager(Card damager) {
        this.damager = damager;
    }

    public Card getDamaged() {
        return damaged;
    }

    public void setDamaged(Card damaged) {
        this.damaged = damaged;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Player getDamagerController() {
        return damagerController;
    }

    public void setDamagerController(Player damagerController) {
        this.damagerController = damagerController;
    }

    public Player getDamagedController() {
        return damagedController;
    }

    public void setDamagedController(Player damagedController) {
        this.damagedController = damagedController;
    }

    @Override
    public String toString() {
        String damagerStr = damager != null ? damagerController.getName() + " " + damager.getName() : "";
        return damagerStr + " has done " + amount + " damage to " + damagedController.getName() + " " + damaged.getName();
    }
}
