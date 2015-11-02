package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Entity;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.parse.domain.Zone;
import org.springframework.stereotype.Component;

@Component
public class ActionFactory {

    public Turn createTurn(int turnNumber) {
        return new Turn(turnNumber, this);
    }

    public Kill createKill(Player beneficiary, Card killer, Card killed, boolean favorableTrade, boolean evenTrade) {
        return new Kill(beneficiary, killer, killed, favorableTrade, evenTrade);
    }

    public Damage createDamage(Card damager, Card damaged, int amount) {
        return new Damage(damager, damaged, amount);
    }

    public CardCreation createCardCreation(Player beneficiary, Card creator, Card created) {
         return new CardCreation(beneficiary, creator, created);
    }

    public HeroHealthChange createHeroHealthChange(Card card, int health) {
         return new HeroHealthChange(card, health);
    }

    public ArmorChange createArmorChange(Card card, int armor) {
        return new ArmorChange(card, armor);
    }

    public CardDrawn createCardDrawn(Player beneficiary, Card card, Entity trigger) {
         return new CardDrawn(beneficiary, card, trigger);
    }

    public CardPlayed createCardPlayed(Player beneficiary, Card card) {
         return new CardPlayed(beneficiary, card);
    }
    
    public ManaGained createManaGained(int mana) {
         return new ManaGained(mana);
    }

    public ManaUsed createManaUsed(Card card, int mana) {
         return new ManaUsed(card, mana);
    }

    public ManaSaved createManaSaved(Card card, int mana) {
         return new ManaSaved(card, mana);
    }

    public ManaOverspent createManaOverspent(Card card, int mana) {
         return new ManaOverspent(card, mana);
    }

    public TempManaGained createTempManaGained(Card card, int mana) {
         return new TempManaGained(card, mana);
    }

    public Attached createAttached(Card card, Card attachedTo) {
         return new Attached(card, attachedTo);
    }

    public Detached createDetached(Card card, Card detachedFrom) {
        return new Detached(card, detachedFrom);
    }

    public Trigger createTrigger(Card card) {
        return new Trigger(card);
    }

    public HealthChange createHealthChange(Card card, int amount) {
        return new HealthChange(card, amount);
    }

    public AttackChange createAttackChange(Card card, int amount) {
        return new AttackChange(card, amount);
    }

    public Joust createJoust(Player friendly, Player opposing, String friendlyCardId, String oppsosingCardId, Card card, boolean winner) {
        return new Joust(friendly, opposing, friendlyCardId, oppsosingCardId, card, winner);
    }

    public ZonePositionChange createZonePositionChange(Card card, Zone zone, int position) {
        return new ZonePositionChange(card, zone, position);
    }

    public HeroPowerUsed createHeroPowerUsed(Player player, Card card) {
        return new HeroPowerUsed(player, card);
    }

    public NumOptions createNumOptions(int number) {
        return new NumOptions(number);
    }

}