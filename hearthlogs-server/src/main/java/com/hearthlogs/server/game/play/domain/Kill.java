package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class Kill implements Action, Serializable {

    private String kind;
    private Player killerController;
    private Player killedController;
    private Player beneficiary;
    private Card killer;
    private Card killed;
    private boolean favorableTrade;
    private boolean evenTrade;

    public Kill(String kind, Player killerController, Player killedController, Player beneficiary, Card killer, Card killed, boolean favorableTrade, boolean evenTrade) {
        this.kind = kind;
        this.killerController = killerController;
        this.killedController = killedController;
        this.beneficiary = beneficiary;
        this.killer = killer;
        this.killed = killed;
        this.favorableTrade = favorableTrade;
        this.evenTrade = evenTrade;
    }

    public Player getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Player beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Card getKiller() {
        return killer;
    }

    public void setKiller(Card killer) {
        this.killer = killer;
    }

    public Card getKilled() {
        return killed;
    }

    public void setKilled(Card killed) {
        this.killed = killed;
    }

    public boolean isFavorableTrade() {
        return favorableTrade;
    }

    public void setFavorableTrade(boolean favorableTrade) {
        this.favorableTrade = favorableTrade;
    }

    public boolean isEvenTrade() {
        return evenTrade;
    }

    public void setEvenTrade(boolean evenTrade) {
        this.evenTrade = evenTrade;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Player getKillerController() {
        return killerController;
    }

    public void setKillerController(Player killerController) {
        this.killerController = killerController;
    }

    public Player getKilledController() {
        return killedController;
    }

    public void setKilledController(Player killedController) {
        this.killedController = killedController;
    }

    @Override
    public String toString() {
        String msg = getMessage(favorableTrade, evenTrade, beneficiary == killerController);
        return killerController.getName() + " " + killer.getName() + " has " + kind + " " + killedController.getName() + " " + killed.getName() + msg;
    }

    private String getMessage(boolean favorableTrade, boolean evenTrade, boolean killerFriendly) {
        String msg;
        String favoring = killerFriendly ? killerController.getName() : killedController.getName();
        if (favorableTrade) {
            msg = " (favorable) " + favoring;
        } else if (evenTrade) {
            msg = " (even) " + favoring;
        } else {
            msg = " (poor) " + favoring;
        }
        return msg;
    }
}
