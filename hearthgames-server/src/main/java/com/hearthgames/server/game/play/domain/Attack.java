package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.AttackSerializer;

@JsonSerialize(using = AttackSerializer.class)
public class Attack implements Action {

    private Card attacker;
    private Card defender;
    private Player attackerController;
    private Player defenderController;

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

    @Override
    public String toString() {
        return attackerController.getName() + " " + attacker.getName() + " is attacking " + defenderController.getName() + " " + defenderController.getName();
    }
}
