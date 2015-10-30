package com.hearthlogs.server.match.result;

import com.hearthlogs.server.match.domain.Card;
import com.hearthlogs.server.match.domain.Player;
import com.hearthlogs.server.match.result.action.*;

import java.util.*;

public class Turn {

    private Long id;

    private int turnNumber;

    private Player whoseTurn;

    private int tempManaUsed; // this is the amount that was JUST used and should be zero'd out after read

    private List<Action> actions = new ArrayList<>();

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void addKill(Card killer, Card killed) {
        Kill kill = new Kill();
        kill.setKiller(killer);
        kill.setKilled(killed);
        actions.add(kill);
    }

    public void addDamage(Card damager, Card damaged, int amount) {
        Damage damage = new Damage();
        damage.setDamager(damager);
        damage.setDamaged(damaged);
        damage.setAmount(amount);
        actions.add(damage);
    }

    public void addCardCreation(Player beneficiary, Card creator, Card created) {
        CardCreation creation = new CardCreation();
        creation.setBeneficiary(beneficiary);
        creation.setCreator(creator);
        creation.setCreated(created);
        actions.add(creation);
    }

    public void addHealthChange(Card card, int health) {
        HealthChange healthChange = new HealthChange();
        healthChange.setCard(card);
        healthChange.setHealth(health);
        actions.add(healthChange);
    }

    public void addCardDrawn(Player beneficiary, Card card) {
        CardDrawn cardDrawn = new CardDrawn();
        cardDrawn.setBeneficiary(beneficiary);
        cardDrawn.setCard(card);
        actions.add(cardDrawn);
    }

    public void addCardPlayed(Player beneficiary, Card card) {
        CardPlayed cardPlayed = new CardPlayed();
        cardPlayed.setBeneficiary(beneficiary);
        cardPlayed.setCard(card);
        actions.add(cardPlayed);
    }

    public Turn(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public void addManaGained(int mana) {
        ManaGained manaGained = new ManaGained();
        manaGained.setManaGained(mana);
        this.actions.add(manaGained);
    }

    public void addManaUsed(Card card, int mana) {
        ManaUsed manaUsed = new ManaUsed();
        manaUsed.setManaUsed(mana);
        manaUsed.setCard(card);
        this.actions.add(manaUsed);
    }

    public void addManaSaved(Card card, int mana) {
        ManaSaved manaSaved = new ManaSaved();
        manaSaved.setManaSaved(mana);
        manaSaved.setCard(card);
        this.actions.add(manaSaved);
    }

    public void addManaOverspent(Card card, int mana) {
        ManaOverspent manaOverspent = new ManaOverspent();
        manaOverspent.setManaOverspent(mana);
        manaOverspent.setCard(card);
        this.actions.add(manaOverspent);
    }

    public void addTempManaGained(Card card, int mana) {
        TempManaGained tempManaGained = new TempManaGained();
        tempManaGained.setCard(card);
        tempManaGained.setTempManaGained(mana);
        this.actions.add(tempManaGained);
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
}
