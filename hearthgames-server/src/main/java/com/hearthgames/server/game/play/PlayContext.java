package com.hearthgames.server.game.play;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.domain.*;
import com.hearthgames.server.game.play.domain.board.Board;

public class PlayContext {

    private GameContext context;
    private GameResult result;

    private Activity activity;

    private Action lastAction = null;
    private Action lastActionProcessed = null;

    public PlayContext(GameContext context, GameResult result) {
        this.context = context;
        this.result = result;
    }

    public GameContext getContext() {
        return context;
    }

    public void setContext(GameContext context) {
        this.context = context;
    }

    public GameResult getResult() {
        return result;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }

    public Activity getActivity() {
        return activity;
    }

    public Card getBefore() {
        return context.getBefore(activity);
    }

    public Card getAfter() {
        return activity.getDelta();
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void addLoggingAction(String msg) {
        addAction(new LoggingAction(msg));
    }

    public void addKill(String kind, Player killerController, Player killedController, Player beneficiary, Card killer, Card killed, boolean favorableTrade, boolean evenTrade) {
        addAction(new Kill(kind, killerController, killedController, beneficiary, killer, killed, favorableTrade, evenTrade));
    }

    public void addDamage(Player damagerController, Player damagedController, Card damager, Card damaged, int amount) {
        addAction(new Damage(damagerController, damagedController, damager, damaged, amount));
    }

    public void addCardCreation(Player creatorController, Player createdController, Player beneficiary, Card creator, Card created) {
        addAction(new CardCreation(creatorController, createdController, beneficiary, creator, created));
    }

    public void addHeroHealthChange(Player player, Card card, int health) {
        addAction(new HeroHealthChange(player, card, health));
    }

    public void addArmorChange(Player player, Card card, int armor) {
        addAction(new ArmorChange(player, card, armor));
    }

    public void addCardDrawn(Player beneficiary, Card card, Card trigger) {
        addAction(new CardDrawn(beneficiary, card, trigger));
    }

    public void addCardPlayed(Zone fromZone, Zone toZone, Player beneficiary, Card card) {
        addAction(new CardPlayed(fromZone, toZone, beneficiary, card));
    }

    public void addCardDiscarded(Player causeController, Player cardController, Zone fromZone, Zone toZone, Player player, Card card, Card cause) {
        addAction(new CardDiscarded(causeController, cardController, fromZone, toZone, player, card, cause));
    }

    public void addManaGained(Player player, int mana) {
        addAction(new ManaGained(player, mana));
    }

    public void addManaUsed(Player player, Card entity, int mana) {
        addAction(new ManaUsed(player, entity, mana));
    }

    public void addManaSaved(Card card, int mana) {
        addAction(new ManaSaved(card, mana));
    }

    public void addManaLost(Card card, int mana) {
        addAction(new ManaLost(card, mana));
    }

    public void addTempManaGained(Player player, Card card, int mana) {
        addAction(new TempManaGained(player, card, mana));
    }

    public void addFrozen(Player player, Card card, boolean frozen) {
        addAction(new Frozen(player, card, frozen));
    }

    public void addAttached(Card card, Card attachedTo) {
        addAction(new Attached(card, attachedTo));
    }

    public void addDetached(Card card, Card detachedFrom) {
        addAction(new Detached(card, detachedFrom));
    }

    public void addTrigger(Player cardController, Card card) {
        addAction(new Trigger(cardController, card));
    }

    public void addHealthChange(Player player, Card card, int amount, int newHealth) {
        addAction(new HealthChange(player, card, amount, newHealth));
    }

    public void addAttackChange(Player player, Card card, int amount, int newAttack) {
        addAction(new AttackChange(player, card, amount, newAttack));
    }

    public void addJoust(Player friendly, Player opposing, Card friendlyJouster, Card opposingJouster, Card card, boolean winner) {
        addAction(new Joust(friendly, opposing, friendlyJouster, opposingJouster, card, winner));
    }

    public void addAttack(Card attacker, Card defender, Player attackerController, Player defenderController) {
        addAction(new Attack(attacker, defender, attackerController, defenderController));
    }

    public void addAttack(Card attacker, Card defender, Card originalDefender, Player attackerController, Player defenderController, Player originalDefenderController) {
        addAction(new Attack(attacker, defender, originalDefender, attackerController, defenderController, originalDefenderController));
    }

    public void addHeroPowerUsed(Player player, Card card) {
        addAction(new HeroPowerUsed(player, card));
    }

    public void addEndofTurn() {
        // we add this so that if the last action is a ZonePositionChange we can capture it with the code below
        addAction(new EndOfTurn());
    }

    private void addAction(Action action) {

        if (action instanceof Attack) {
            result.getCurrentTurn().addAction(new LoggingAction("Combat!"));
            result.addActionLog("Combat!");
            addBoard(result);
        }

        lastAction = action;
        result.getCurrentTurn().addAction(action);
        result.addActionLog(action.toString());
    }

    public void addFirstBoard() {
        addBoard(result);
    }
    public void addLastBoard() {
        addBoard(result);
    }

    public void addBoard() {
        if (lastAction != null && lastAction != lastActionProcessed) {
            lastActionProcessed = lastAction;
            if (result.getCurrentTurn().getTurnNumber() > 0 && !(lastAction instanceof LoggingAction)) {
                addBoard(result);
            }
        }
    }

    public Action getLastAction() {
        return lastAction;
    }

    private void addBoard(GameResult result) {
        result.getCurrentTurn().addAction(new Board(result, context));
    }
}
