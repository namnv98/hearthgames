package com.hearthgames.server.game.play;

import com.hearthgames.server.game.parse.domain.*;
import com.hearthgames.server.game.play.domain.*;

import java.util.*;

public class GameResult {

    private Player winner;
    private Player loser;
    private Player quitter;
    private Player first;

    private Integer rank;

    private Set<Card> friendlyStartingCards = new LinkedHashSet<>();
    private Set<Card> opposingStartingCards = new LinkedHashSet<>();

    private Set<Card> friendlyMulliganedCards = new LinkedHashSet<>();
    private Set<Card> opposingMulliganedCards = new LinkedHashSet<>();

    private Set<Card> friendlyDeckCards = new LinkedHashSet<>();
    private Set<Card> opposingDeckCards = new LinkedHashSet<>();

    private String winnerClass; // the class (i.e. warrior, hunter, etc...)
    private String loserClass;

    private Set<Turn> turns = new LinkedHashSet<>();
    private Turn currentTurn;
    private int turnNumber;

    private List<String> actionLogs = new ArrayList<>();

    public void addActionLog(String log) {
        System.out.println(log);
        actionLogs.add(log);
    }

    public List<String> getActionLogs() {
        return actionLogs;
    }

    public void addFriendlyStartingCard(Card card) {
        friendlyStartingCards.add(card);
        if (!Card.THE_COIN.equals(card.getCardid())) {
            friendlyDeckCards.add(card);
        }
    }

    public void addOpposingStartingCard(Card card) {
        opposingStartingCards.add(card);
        if (!Card.THE_COIN.equals(card.getCardid())) {
            opposingDeckCards.add(card);
        }
    }

    public void mulliganFriendlyCard(Card card) {
        friendlyMulliganedCards.add(card);
        friendlyStartingCards.remove(card);
    }

    public void mulliganOpposingCard(Card card) {
        opposingMulliganedCards.add(card);
        opposingStartingCards.remove(card);
    }

    public void addFriendlyDeckCard(Card card) {
        friendlyDeckCards.add(card);
    }

    public void addOpposingDeckCard(Card card) {
        opposingDeckCards.add(card);
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

    public Set<Card> getOpposingDeckCards() {
        return opposingDeckCards;
    }

    public Set<Card> getFriendlyDeckCards() {
        return friendlyDeckCards;
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

    public Turn getTurnBefore(Turn turn) {
        Turn lastTurn = null;
        for (Turn t: turns) {
            if (t == turn) {
                return lastTurn;
            }
            lastTurn = t;
        }
        return lastTurn;
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

    public void addTurn() {
        currentTurn = new Turn(turnNumber);
        this.turns.add(currentTurn);
    }

    public void addLoggingAction(String msg) {
        LoggingAction loggingAction = new LoggingAction(msg);
        this.currentTurn.addAction(loggingAction);
        addActionLog(msg);
    }

    public void addKill(String kind, Player killerController, Player killedController, Player beneficiary, Card killer, Card killed, boolean favorableTrade, boolean evenTrade) {
        Kill kill = new Kill(kind, killerController, killedController, beneficiary, killer, killed, favorableTrade, evenTrade);
        this.currentTurn.addAction(kill);
        addActionLog(kill.toString());
    }

    public void addDamage(Player damagerController, Player damagedController, Card damager, Card damaged, int amount) {
        Damage damage = new Damage(damagerController, damagedController, damager, damaged, amount);
        this.currentTurn.addAction(damage);
        addActionLog(damage.toString());
    }

    public void addCardCreation(Player creatorController, Player createdController, Player beneficiary, Card creator, Card created) {
        CardCreation cardCreation = new CardCreation(creatorController, createdController, beneficiary, creator, created);
        this.currentTurn.addAction(cardCreation);
        addActionLog(cardCreation.toString());
    }

    public void addHeroHealthChange(Player player, Card card, int health) {
        HeroHealthChange heroHealthChange = new HeroHealthChange(player, card, health);
        this.currentTurn.addAction(heroHealthChange);
        addActionLog(heroHealthChange.toString());
    }

    public void addArmorChange(Player player, Card card, int armor) {
        ArmorChange armorChange = new ArmorChange(player, card, armor);
        this.currentTurn.addAction(armorChange);
        addActionLog(armorChange.toString());
    }

    public void addCardDrawn(Player beneficiary, Card card, Entity trigger) {
        CardDrawn cardDrawn = new CardDrawn(beneficiary, card, trigger);
        this.currentTurn.addAction(cardDrawn);
        addActionLog(cardDrawn.toString());
    }

    public void addCardPlayed(Zone fromZone, Zone toZone, Player beneficiary, Card card) {
        CardPlayed cardPlayed = new CardPlayed(fromZone, toZone, beneficiary, card);
        this.currentTurn.addAction(cardPlayed);
        addActionLog(cardPlayed.toString());
    }

    public void addCardDiscarded(Player causeController, Player cardController, Zone fromZone, Zone toZone, Player player, Card card, Card cause) {
        CardDiscarded cardDiscarded = new CardDiscarded(causeController, cardController, fromZone, toZone, player, card, cause);
        this.currentTurn.addAction(cardDiscarded);
        addActionLog(cardDiscarded.toString());
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

    public void addFrozen(Player player, Card card, boolean frozen) {
        Frozen frozenAction = new Frozen(player, card, frozen);
        this.currentTurn.addAction(frozenAction);
        addActionLog(frozenAction.toString());
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

    public void addHealthChange(Player player, Card card, int amount, int newHealth) {
        HealthChange healthChange = new HealthChange(player, card, amount, newHealth);
        this.currentTurn.addAction(healthChange);
        addActionLog(healthChange.toString());
    }

    public void addAttackChange(Player player, Card card, int amount, int newAttack) {
        AttackChange attackChange = new AttackChange(player, card, amount, newAttack);
        this.currentTurn.addAction(attackChange);
        addActionLog(attackChange.toString());
    }

    public void addJoust(Player friendly, Player opposing, Card friendlyJouster, Card oppsosingJouster, Card card, boolean winner) {
        Joust joust = new Joust(friendly, opposing, friendlyJouster, oppsosingJouster, card, winner);
        this.currentTurn.addAction(joust);
        addActionLog(joust.toString());
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
        HeroPowerUsed heroPowerUsed = new HeroPowerUsed(player, card);
        this.currentTurn.addAction(heroPowerUsed);
        addActionLog(heroPowerUsed.toString());
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
            } else if (lastAction instanceof LoggingAction) {
                LoggingAction loggingAction = (LoggingAction) lastAction;
                return "Mulligan Phase has started".equals(loggingAction.getMsg()) || "Mulligan Phase has ended".equals(loggingAction.getMsg());
            } else if (lastAction instanceof EndOfTurn) {
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

    private Activity lastActivityProcessed;

    public Activity getLastActivityProcessed() {
        return lastActivityProcessed;
    }

    public void setLastActivityProcessed(Activity lastActivityProcessed) {
        this.lastActivityProcessed = lastActivityProcessed;
    }
}
