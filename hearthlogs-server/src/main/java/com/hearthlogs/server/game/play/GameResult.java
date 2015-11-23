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

    public void addKill(String kind, String killerSide, String killedSide, Player beneficiary, Card killer, Card killed, boolean favorableTrade, boolean evenTrade) {
        this.currentTurn.addAction(new Kill(kind, killerSide, killedSide, beneficiary, killer, killed, favorableTrade, evenTrade));
    }

    public void addDamage(String damagerSide, String damagedSide, Card damager, Card damaged, int amount) {
        this.currentTurn.addAction(new Damage(damagerSide, damagedSide, damager, damaged, amount));
    }

    public void addCardCreation(String creatorSide, String createdSide, Player beneficiary, Card creator, Card created) {
        this.currentTurn.addAction(new CardCreation(creatorSide, createdSide, beneficiary, creator, created));
    }

    public void addHeroHealthChange(String side, Card card, int health) {
        this.currentTurn.addAction(new HeroHealthChange(side, card, health));
    }

    public void addArmorChange(String side, Card card, int armor) {
        this.currentTurn.addAction(new ArmorChange(side, card, armor));
    }

    public void addCardDrawn(Player beneficiary, Card card, Entity trigger) {
        this.currentTurn.addAction(new CardDrawn(beneficiary, card, trigger));
    }

    public void addCardPlayed(Zone fromZone, Zone toZone, Player beneficiary, Card card) {
        this.currentTurn.addAction(new CardPlayed(fromZone, toZone, beneficiary, card));
    }

    public void addCardDiscarded(String causeSide, String cardSide, Zone fromZone, Zone toZone, Player player, Card card, Card cause) {
        this.currentTurn.addAction(new CardDiscarded(causeSide, cardSide, fromZone, toZone, player, card, cause));
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

    public void addFrozen(String side, Card card, boolean frozen) {
        this.currentTurn.addAction(new Frozen(side, card, frozen));
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

    public void addHealthChange(String side, Card card, int amount, int newHealth) {
        this.currentTurn.addAction(new HealthChange(side, card, amount, newHealth));
    }

    public void addAttackChange(String side, Card card, int amount, int newAttack) {
        this.currentTurn.addAction(new AttackChange(side, card, amount, newAttack));
    }

    public void addJoust(Player friendly, Player opposing, String friendlyCardId, String oppsosingCardId, Card card, boolean winner) {
        this.currentTurn.addAction(new Joust(friendly, opposing, friendlyCardId, oppsosingCardId, card, winner));
    }

    public void addZonePositionChange(Card card, Zone zone, int position) {
        int size = this.getCurrentTurn().getActions().size();
        Action lastAction = size > 0 ? this.getCurrentTurn().getActions().get(size-1) : null;
        ZonePositionChange zonePositionChange = new ZonePositionChange(card, zone, position);
        if (lastAction instanceof ZonePositionChange) {
            ((ZonePositionChange) lastAction).addZonePositionChange(zonePositionChange);
        } else {
            this.currentTurn.addAction(zonePositionChange);
        }
    }

    public void addHeroPowerUsed(Player player, Card card) {
        this.currentTurn.addAction(new HeroPowerUsed(player, card));
    }

    public void addNumOptions(int number) {
        this.currentTurn.addAction(new NumOptions(number));
    }

    public void addEndofTurn() {
        // we add this so that if the last action is a ZonePositionChange we can capture it with the code below
        this.currentTurn.addAction(new EndOfTurn());
    }

    // keep track of the last action we looked at for determining if we should update the board state so we don't update every time a tag changes..we only want
    // to look at when another action was added but calling isUpdateBoardState will happen after we update the game state.
    private Action lastActionProcessed = null;

    public boolean isUpdateBoardState() {
        int size = this.getCurrentTurn().getActions().size();
        Action lastAction = size > 0 ? getCurrentTurn().getActions().get(size-1) : null;
        if (lastAction != null && lastActionProcessed != lastAction) {
            lastActionProcessed = lastAction;
            if (lastAction instanceof CardPlayed || lastAction instanceof CardDrawn || lastAction instanceof Kill ||
                lastAction instanceof Damage || lastAction instanceof Frozen || lastAction instanceof CardDiscarded) {
                if (lastAction instanceof Damage) {
                    Damage damage = (Damage) lastAction;
                    if (Card.Type.HERO.eq(damage.getDamaged().getCardtype())) {
                        return false;
                    }
                }
                if (lastAction instanceof CardPlayed) {
                    CardPlayed cardPlayed = (CardPlayed) lastAction;
                    if (cardPlayed.getCard().isSpell()) {
                        return false;
                    }
                }
                return true;
            } else {
                if (size > 1) {
                    Action beforeLastAction = this.getCurrentTurn().getActions().get(size-2);
                    if (beforeLastAction instanceof ZonePositionChange) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
