package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.AttackSerializer;

import java.io.Serializable;

@JsonSerialize(using = AttackSerializer.class)
public class Attack implements Action, Serializable {

    private Card attacker;
    private Card defender;
    private Card originalDefender;
    private Player attackerController;
    private Player defenderController;
    private Player originalDefenderController;

    public Attack(Card attacker, Card defender, Card originalDefender, Player attackerController, Player defenderController, Player originalDefenderController) {
        this.attacker = attacker;
        this.defender = defender;
        this.originalDefender = originalDefender;
        this.attackerController = attackerController;
        this.defenderController = defenderController;
        this.originalDefenderController = originalDefenderController;
    }

    public Attack(Card attacker, Card defender, Player attackerController, Player defenderController) {
        this.attacker = attacker;
        this.defender = defender;
        this.attackerController = attackerController;
        this.defenderController = defenderController;
    }

    public Card getAttacker() {
        return attacker;
    }

    public void setAttacker(Card attacker) {
        this.attacker = attacker;
    }

    public Card getDefender() {
        return defender;
    }

    public void setDefender(Card defender) {
        this.defender = defender;
    }

    public Card getOriginalDefender() {
        return originalDefender;
    }

    public void setOriginalDefender(Card originalDefender) {
        this.originalDefender = originalDefender;
    }

    public Player getAttackerController() {
        return attackerController;
    }

    public void setAttackerController(Player attackerController) {
        this.attackerController = attackerController;
    }

    public Player getDefenderController() {
        return defenderController;
    }

    public void setDefenderController(Player defenderController) {
        this.defenderController = defenderController;
    }

    public Player getOriginalDefenderController() {
        return originalDefenderController;
    }

    public void setOriginalDefenderController(Player originalDefenderController) {
        this.originalDefenderController = originalDefenderController;
    }

    @Override
    public String toString() {
        if (originalDefenderController != null) {
            String original = attackerController.getName() + " " + attacker.getName() + " is attacking " + originalDefenderController.getName() + " " + originalDefender.getName();
            return original + " but instead attacked " + defenderController.getName() + " " + defender.getName();
        }
        return attackerController.getName() + " " + attacker.getName() + " is attacking " + defenderController.getName() + " " + defender.getName();
    }
}
