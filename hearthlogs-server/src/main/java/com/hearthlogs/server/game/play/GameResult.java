package com.hearthlogs.server.game.play;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Zone;
import com.hearthlogs.server.game.play.domain.*;
import com.hearthlogs.server.game.parse.domain.Entity;
import com.hearthlogs.server.game.parse.domain.Player;

import java.util.*;

public class GameResult {

    private Player friendly;
    private Player opposing;
    private Player winner;
    private Player loser;
    private Player quitter;
    private Player first;

    private Integer rank;

    private Set<Card> friendlyStartingCards = new LinkedHashSet<>();
    private Set<Card> opposingStartingCards = new LinkedHashSet<>();

    private Set<Card> friendlyMulliganedCards = new LinkedHashSet<>();
    private Set<Card> opposingMulliganedCards = new LinkedHashSet<>();

    private String winnerClass; // the class (i.e. warrior, hunter, etc...)
    private String loserClass;

    private Set<Turn> turns = new LinkedHashSet<>();
    private Turn currentTurn;
    private int turnNumber;

    public void addFriendlyStartingCard(Card card) {
        friendlyStartingCards.add(card);
    }

    public void addOpposingStartingCard(Card card) {
        opposingStartingCards.add(card);
    }

    public void mulliganFriendlyCard(Card card) {
        friendlyMulliganedCards.add(card);
        friendlyStartingCards.remove(card);
    }

    public void mulliganOpposingCard(Card card) {
        opposingMulliganedCards.add(card);
        opposingStartingCards.remove(card);
    }

    public Set<Card> getFriendlyStartingCards() {
        return friendlyStartingCards;
    }

    public Set<Card> getOpposingStartingCards() {
        return opposingStartingCards;
    }

    public Set<Card> getFriendlyMulliganedCards() {
        return friendlyMulliganedCards;
    }

    public Set<Card> getOpposingMulliganedCards() {
        return opposingMulliganedCards;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getLoser() {
        return loser;
    }

    public void setLoser(Player loser) {
        this.loser = loser;
    }

    public Player getQuitter() {
        return quitter;
    }

    public void setQuitter(Player quitter) {
        this.quitter = quitter;
    }

    public Player getFirst() {
        return first;
    }

    public void setFirst(Player first) {
        this.first = first;
    }

    public Set<Turn> getTurns() {
        return turns;
    }

    public void setTurns(Set<Turn> turns) {
        this.turns = turns;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public void setFriendlyStartingCards(Set<Card> friendlyStartingCards) {
        this.friendlyStartingCards = friendlyStartingCards;
    }

    public void setOpposingStartingCards(Set<Card> opposingStartingCards) {
        this.opposingStartingCards = opposingStartingCards;
    }

    public void setFriendlyMulliganedCards(Set<Card> friendlyMulliganedCards) {
        this.friendlyMulliganedCards = friendlyMulliganedCards;
    }

    public void setOpposingMulliganedCards(Set<Card> opposingMulliganedCards) {
        this.opposingMulliganedCards = opposingMulliganedCards;
    }

    public String getWinnerClass() {
        return winnerClass;
    }

    public void setWinnerClass(String winnerClass) {
        this.winnerClass = winnerClass;
    }

    public String getLoserClass() {
        return loserClass;
    }

    public void setLoserClass(String loserClass) {
        this.loserClass = loserClass;
    }

    public void setCurrentTurn(Turn currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public Player getFriendly() {
        return friendly;
    }

    public void setFriendly(Player friendly) {
        this.friendly = friendly;
    }

    public Player getOpposing() {
        return opposing;
    }

    public void setOpposing(Player opposing) {
        this.opposing = opposing;
    }

    public void addTurn() {
        currentTurn = new Turn(turnNumber);
        this.turns.add(currentTurn);
    }

    public void addKill(Player beneficiary, Card killer, Card killed, boolean favorableTrade, boolean evenTrade) {
        this.currentTurn.addAction(new Kill(beneficiary, killer, killed, favorableTrade, evenTrade));
    }

    public void addDamage(Card damager, Card damaged, int amount) {
        this.currentTurn.addAction(new Damage(damager, damaged, amount));
    }

    public void addCardCreation(Player beneficiary, Card creator, Card created) {
        this.currentTurn.addAction(new CardCreation(beneficiary, creator, created));
    }

    public void addHeroHealthChange(Card card, int health) {
        this.currentTurn.addAction(new HeroHealthChange(card, health));
    }

    public void addArmorChange(Card card, int armor) {
        this.currentTurn.addAction(new ArmorChange(card, armor));
    }

    public void addCardDrawn(Player beneficiary, Card card, Entity trigger) {
        this.currentTurn.addAction(new CardDrawn(beneficiary, card, trigger));
    }

    public void addCardPlayed(Player beneficiary, Card card) {
        this.currentTurn.addAction(new CardPlayed(beneficiary, card));
    }

    public void addManaGained(int mana) {
        this.currentTurn.addAction(new ManaGained(mana));
    }

    public void addManaUsed(Entity entity, int mana) {
        this.currentTurn.addAction(new ManaUsed(entity, mana));
    }

    public void addManaSaved(Card card, int mana) {
        this.currentTurn.addAction(new ManaSaved(card, mana));
    }

    public void addManaLost(Card card, int mana) {
        this.currentTurn.addAction(new ManaLost(card, mana));
    }

    public void addTempManaGained(Card card, int mana) {
        this.currentTurn.addAction(new TempManaGained(card, mana));
    }

    public void addFrozen(Card card, boolean frozen) {
        this.currentTurn.addAction(new Frozen(card, frozen));
    }

    public void addAttached(Card card, Card attachedTo) {
        this.currentTurn.addAction(new Attached(card, attachedTo));
    }

    public void addDetached(Card card, Card detachedFrom) {
        this.currentTurn.addAction(new Detached(card, detachedFrom));
    }

    public void addTrigger(Card card) {
        this.currentTurn.addAction(new Trigger(card));
    }

    public void addHealthChange(Card card, int amount) {
        this.currentTurn.addAction(new HealthChange(card, amount));
    }

    public void addAttackChange(Card card, int amount) {
        this.currentTurn.addAction(new AttackChange(card, amount));
    }

    public void addJoust(Player friendly, Player opposing, String friendlyCardId, String oppsosingCardId, Card card, boolean winner) {
        this.currentTurn.addAction(new Joust(friendly, opposing, friendlyCardId, oppsosingCardId, card, winner));
    }

    public void addZonePositionChange(Card card, Zone zone, int position) {
        this.currentTurn.addAction(new ZonePositionChange(card, zone, position));
    }

    public void addHeroPowerUsed(Player player, Card card) {
        this.currentTurn.addAction(new HeroPowerUsed(player, card));
    }

    public void addNumOptions(int number) {
        this.currentTurn.addAction(new NumOptions(number));
    }


}
