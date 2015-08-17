package com.hearthlogs.web.domain;

import java.io.Serializable;

/**
 * This represents one of the Entity found in the Hearthstone log file.
 *
 * It is initially sourced at the start of a match.  Cards are also created during the match and appear in ACTION
 *
 * FULL_ENTITY - Creating ID=16 CardID=GVG_069
 *     tag=HEALTH value=3
 *     tag=ATK value=3
 *     tag=COST value=5
 *     tag=ZONE value=HAND
 *     tag=CONTROLLER value=1
 *     tag=ENTITY_ID value=16
 *     tag=CARDTYPE value=MINION
 *     tag=RARITY value=COMMON
 *     tag=BATTLECRY value=1
 *     tag=ZONE_POSITION value=4
 *
 *
 */
public class Card extends Entity implements Serializable {

    private static final long serialVersionUID = 1;

    public enum Type {
        MINION;

        public boolean eq(String type) {
            return this.toString().equals(type);
        }
    }

    private String id;
    private String cardid;
    private String triggerVisual;
    private String health;
    private String atk;
    private String cost;
    private String zone;
    private String controller;
    private String cardtype;
    private String rarity;
    private String zonePosition;
    private String attached;
    private String faction;
    private String cantPlay;
    private String battlecry;
    private String premium;
    private String freeze;
    private String elite;
    private String creator;
    private String deathrattle;
    private String exhausted;
    private String cardClass;
    private String secret;
    private String justPlayed;
    private String ignoreDamageOff;
    private String spellpower;
    private String taunt;
    private String cardTarget;
    private String silence;
    private String linkedcard;
    private String charge;
    private String topdeck;
    private String cantBeDamaged;
    private String numAttacksThisTurn;
    private String attacking;
    private String defending;
    private String ignoreDamage;
    private String aura;
    private String aiOneShotKill;
    private String combo;
    private String toBeDestroyed;
    private String affectedBySpellPower;
    private String windfury;
    private String divineShield;
    private String lastAffectedBy;
    private String recall;
    private String recallOwed;
    private String durability;
    private String damage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getTriggerVisual() {
        return triggerVisual;
    }

    public void setTriggerVisual(String triggerVisual) {
        this.triggerVisual = triggerVisual;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getAtk() {
        return atk;
    }

    public void setAtk(String atk) {
        this.atk = atk;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getZonePosition() {
        return zonePosition;
    }

    public void setZonePosition(String zonePosition) {
        this.zonePosition = zonePosition;
    }

    public String getAttached() {
        return attached;
    }

    public void setAttached(String attached) {
        this.attached = attached;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public String getCantPlay() {
        return cantPlay;
    }

    public void setCantPlay(String cantPlay) {
        this.cantPlay = cantPlay;
    }

    public String getBattlecry() {
        return battlecry;
    }

    public void setBattlecry(String battlecry) {
        this.battlecry = battlecry;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getElite() {
        return elite;
    }

    public void setElite(String elite) {
        this.elite = elite;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDeathrattle() {
        return deathrattle;
    }

    public void setDeathrattle(String deathrattle) {
        this.deathrattle = deathrattle;
    }

    public String getExhausted() {
        return exhausted;
    }

    public void setExhausted(String exhausted) {
        this.exhausted = exhausted;
    }

    public String getCardClass() {
        return cardClass;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getJustPlayed() {
        return justPlayed;
    }

    public void setJustPlayed(String justPlayed) {
        this.justPlayed = justPlayed;
    }

    public String getIgnoreDamageOff() {
        return ignoreDamageOff;
    }

    public void setIgnoreDamageOff(String ignoreDamageOff) {
        this.ignoreDamageOff = ignoreDamageOff;
    }

    public String getSpellpower() {
        return spellpower;
    }

    public void setSpellpower(String spellpower) {
        this.spellpower = spellpower;
    }

    public String getTaunt() {
        return taunt;
    }

    public void setTaunt(String taunt) {
        this.taunt = taunt;
    }

    public String getCardTarget() {
        return cardTarget;
    }

    public void setCardTarget(String cardTarget) {
        this.cardTarget = cardTarget;
    }

    public String getSilence() {
        return silence;
    }

    public void setSilence(String silence) {
        this.silence = silence;
    }

    public String getLinkedcard() {
        return linkedcard;
    }

    public void setLinkedcard(String linkedcard) {
        this.linkedcard = linkedcard;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getTopdeck() {
        return topdeck;
    }

    public void setTopdeck(String topdeck) {
        this.topdeck = topdeck;
    }

    public String getCantBeDamaged() {
        return cantBeDamaged;
    }

    public void setCantBeDamaged(String cantBeDamaged) {
        this.cantBeDamaged = cantBeDamaged;
    }

    public String getNumAttacksThisTurn() {
        return numAttacksThisTurn;
    }

    public void setNumAttacksThisTurn(String numAttacksThisTurn) {
        this.numAttacksThisTurn = numAttacksThisTurn;
    }

    public String getAttacking() {
        return attacking;
    }

    public void setAttacking(String attacking) {
        this.attacking = attacking;
    }

    public String getDefending() {
        return defending;
    }

    public void setDefending(String defending) {
        this.defending = defending;
    }

    public String getIgnoreDamage() {
        return ignoreDamage;
    }

    public void setIgnoreDamage(String ignoreDamage) {
        this.ignoreDamage = ignoreDamage;
    }

    public String getAura() {
        return aura;
    }

    public void setAura(String aura) {
        this.aura = aura;
    }

    public String getAiOneShotKill() {
        return aiOneShotKill;
    }

    public void setAiOneShotKill(String aiOneShotKill) {
        this.aiOneShotKill = aiOneShotKill;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public String getToBeDestroyed() {
        return toBeDestroyed;
    }

    public void setToBeDestroyed(String toBeDestroyed) {
        this.toBeDestroyed = toBeDestroyed;
    }

    public String getAffectedBySpellPower() {
        return affectedBySpellPower;
    }

    public void setAffectedBySpellPower(String affectedBySpellPower) {
        this.affectedBySpellPower = affectedBySpellPower;
    }

    public String getWindfury() {
        return windfury;
    }

    public void setWindfury(String windfury) {
        this.windfury = windfury;
    }

    public String getDivineShield() {
        return divineShield;
    }

    public void setDivineShield(String divineShield) {
        this.divineShield = divineShield;
    }

    public String getLastAffectedBy() {
        return lastAffectedBy;
    }

    public void setLastAffectedBy(String lastAffectedBy) {
        this.lastAffectedBy = lastAffectedBy;
    }

    public String getRecall() {
        return recall;
    }

    public void setRecall(String recall) {
        this.recall = recall;
    }

    public String getRecallOwed() {
        return recallOwed;
    }

    public void setRecallOwed(String recallOwed) {
        this.recallOwed = recallOwed;
    }

    public String getDurability() {
        return durability;
    }

    public void setDurability(String durability) {
        this.durability = durability;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }
}
