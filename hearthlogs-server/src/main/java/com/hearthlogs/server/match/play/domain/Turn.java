package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Entity;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.parse.domain.Zone;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Turn implements Serializable {

    private int turnNumber;
    private ActionFactory actionFactory;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private Player whoseTurn;

    private int tempManaUsed; // this is the amount that was JUST used and should be zero'd out after read

    private Set<Card> friendlyCardsPutInPlay = new LinkedHashSet<>();
    private Set<Card> friendlyCardsRemovedFromPlay = new LinkedHashSet<>();
    private Set<Card> opposingCardsPutInPlay = new LinkedHashSet<>();
    private Set<Card> opposingCardsRemovedFromPlay = new LinkedHashSet<>();

    private Set<Card> friendlyCardsInPlay = new HashSet<>();
    private Set<Card> opposingCardsInPlay = new HashSet<>();

    private List<Action> actions = new ArrayList<>();

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void addKill(Player beneficiary, Card killer, Card killed, boolean favorableTrade, boolean evenTrade) {
        Kill kill = actionFactory.createKill(beneficiary, killer, killed, favorableTrade, evenTrade);
        actions.add(kill);
    }

    public void addDamage(Card damager, Card damaged, int amount) {
        Damage damage = actionFactory.createDamage(damager, damaged, amount);
        actions.add(damage);
    }

    public void addCardCreation(Player beneficiary, Card creator, Card created) {
        CardCreation creation = actionFactory.createCardCreation(beneficiary, creator, created);
        actions.add(creation);
    }

    public void addHeroHealthChange(Card card, int health) {
        HeroHealthChange heroHealthChange = actionFactory.createHeroHealthChange(card, health);
        actions.add(heroHealthChange);
    }

    public void addArmorChange(Card card, int armor) {
        ArmorChange armorChange = actionFactory.createArmorChange(card, armor);
        actions.add(armorChange);
    }

    public void addCardDrawn(Player beneficiary, Card card, Entity trigger) {
        CardDrawn cardDrawn = actionFactory.createCardDrawn(beneficiary, card, trigger);
        actions.add(cardDrawn);
    }

    public void addCardPlayed(Player beneficiary, Card card) {
        CardPlayed cardPlayed = actionFactory.createCardPlayed(beneficiary, card);
        actions.add(cardPlayed);
    }

    public void addTrigger(Card card) {
        Trigger trigger = actionFactory.createTrigger(card);
        actions.add(trigger);
    }

    public void addHealthChange(Card card, int amount) {
        HealthChange healthChange = actionFactory.createHealthChange(card, amount);
        actions.add(healthChange);
    }

    public void addAttackChange(Card card, int amount) {
        AttackChange attackChange = actionFactory.createAttackChange(card, amount);
        actions.add(attackChange);
    }

    public void addManaGained(int mana) {
        ManaGained manaGained = actionFactory.createManaGained(mana);
        this.actions.add(manaGained);
    }

    public void addManaUsed(Card card, int mana) {
        ManaUsed manaUsed = actionFactory.createManaUsed(card, mana);
        this.actions.add(manaUsed);
    }

    public void addManaSaved(Card card, int mana) {
        ManaSaved manaSaved = actionFactory.createManaSaved(card, mana);
        this.actions.add(manaSaved);
    }

    public void addManaOverspent(Card card, int mana) {
        ManaOverspent manaOverspent = actionFactory.createManaOverspent(card, mana);
        this.actions.add(manaOverspent);
    }

    public void addTempManaGained(Card card, int mana) {
        TempManaGained tempManaGained = actionFactory.createTempManaGained(card, mana);
        this.actions.add(tempManaGained);
    }

    public void addAttached(Card card, Card attachedTo) {
        Attached attached = actionFactory.createAttached(card, attachedTo);
        this.actions.add(attached);
    }

    public void addDetached(Card card, Card detachedFrom) {
        Detached detached = actionFactory.createDetached(card, detachedFrom);
        this.actions.add(detached);
    }

    public void addJoust(Player friendly, Player opposing, String friendlyCardId, String oppsosingCardId, Card card, boolean winner) {
        Joust joust = actionFactory.createJoust(friendly, opposing, friendlyCardId, oppsosingCardId, card, winner);
        this.actions.add(joust);
    }

    public void addZonePositionChange(Card card, Zone zone, int position) {
        ZonePositionChange zonePositionChange = actionFactory.createZonePositionChange(card, zone, position);
        this.actions.add(zonePositionChange);
    }

    public void addHeroPowerUsed(Player player, Card card) {
        HeroPowerUsed heroPowerUsed = actionFactory.createHeroPowerUsed(player, card);
        this.actions.add(heroPowerUsed);
    }

    public void addNumOptions(int number) {
        NumOptions numOptions = actionFactory.createNumOptions(number);
        this.actions.add(numOptions);
    }

    public void addFrozen(Card card, boolean frozen) {
        Frozen f = new Frozen(card, frozen);
        this.actions.add(f);
    }

    protected Turn(int turnNumber, ActionFactory actionFactory) {
        this.turnNumber = turnNumber;
        this.actionFactory = actionFactory;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getManaGained() {
        int sum = 0;
        for (Action a: actions) {
            if (a instanceof ManaGained) {
                sum += ((ManaGained) a).getManaGained();
            } else if (a instanceof TempManaGained) {
                sum += ((TempManaGained) a).getTempManaGained();
            }
        }
        return sum;
    }

    public int getManaUsed() {
        int sum = 0;
        for (Action a: actions) {
            if (a instanceof ManaUsed) {
                sum += ((ManaUsed) a).getManaUsed();
            }
        }
        return sum;
    }

    public int getManaSaved() {
        int sum = 0;
        for (Action a: actions) {
            if (a instanceof ManaSaved) {
                sum += ((ManaSaved) a).getManaSaved();
            }
        }
        return sum;
    }

    public int getTempManaUsed() {
        return tempManaUsed;
    }

    public void setTempManaUsed(int tempManaUsed) {
        this.tempManaUsed = tempManaUsed;
    }

    public Player getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(Player whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public Set<Card> getFriendlyCardsPutInPlay() {
        return friendlyCardsPutInPlay;
    }

    public void setFriendlyCardsPutInPlay(Set<Card> friendlyCardsPutInPlay) {
        this.friendlyCardsPutInPlay = friendlyCardsPutInPlay;
    }

    public Set<Card> getFriendlyCardsRemovedFromPlay() {
        return friendlyCardsRemovedFromPlay;
    }

    public void setFriendlyCardsRemovedFromPlay(Set<Card> friendlyCardsRemovedFromPlay) {
        this.friendlyCardsRemovedFromPlay = friendlyCardsRemovedFromPlay;
    }

    public Set<Card> getOpposingCardsPutInPlay() {
        return opposingCardsPutInPlay;
    }

    public void setOpposingCardsPutInPlay(Set<Card> opposingCardsPutInPlay) {
        this.opposingCardsPutInPlay = opposingCardsPutInPlay;
    }

    public Set<Card> getOpposingCardsRemovedFromPlay() {
        return opposingCardsRemovedFromPlay;
    }

    public void setOpposingCardsRemovedFromPlay(Set<Card> opposingCardsRemovedFromPlay) {
        this.opposingCardsRemovedFromPlay = opposingCardsRemovedFromPlay;
    }


    public Set<Card> getFriendlyCardsInPlay() {
        return friendlyCardsInPlay;
    }

    public void setFriendlyCardsInPlay(Set<Card> friendlyCardsInPlay) {
        this.friendlyCardsInPlay = friendlyCardsInPlay;
    }

    public Set<Card> getOpposingCardsInPlay() {
        return opposingCardsInPlay;
    }

    public void setOpposingCardsInPlay(Set<Card> opposingCardsInPlay) {
        this.opposingCardsInPlay = opposingCardsInPlay;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
