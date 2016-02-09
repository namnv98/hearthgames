package com.hearthgames.server.game.parse.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
public class Card implements Serializable {

    private static final long serialVersionUID = 1;

    public static final String THE_COIN = "GAME_005";

    public static final Card UNKNOWN;

    static {
        UNKNOWN = new Card();
        UNKNOWN.setEntityId("0");
    }

    public enum Type {
        MINION,
        SPELL,
        ENCHANTMENT,
        WEAPON,
        HERO,
        HERO_POWER,
        PLAYER,
        GAME;

        public boolean eq(String type) {
            return this.toString().equalsIgnoreCase(type);
        }
    }

    protected String entityId;
    protected Map<String, String> unknownTags = new HashMap<>();

    protected String cardid;
    protected String triggerVisual;
    protected String health;
    protected String armor;
    protected String atk;
    protected String cost;
    protected String zone;
    protected String controller;
    protected String cardtype;
    protected String rarity;
    protected String zonePosition;
    protected String attached;
    protected String faction;
    protected String cantPlay;
    protected String battlecry;
    protected String premium;
    protected String freeze;
    protected String elite;
    protected String creator;
    protected String deathrattle;
    protected String exhausted;
    protected String cardClass;
    protected String secret;
    protected String justPlayed;
    protected String ignoreDamageOff;
    protected String spellpower;
    protected String taunt;
    protected String cardTarget;
    protected String silence;
    protected String linkedcard;
    protected String charge;
    protected String topdeck;
    protected String cantBeDamaged;
    protected String numAttacksThisTurn;
    protected String attacking;
    protected String defending;
    protected String ignoreDamage;
    protected String aura;
    protected String aiOneShotKill;
    protected String combo;
    protected String toBeDestroyed;
    protected String affectedBySpellPower;
    protected String windfury;
    protected String divineShield;
    protected String lastAffectedBy;
    protected String recall;
    protected String recallOwed;
    protected String durability;
    protected String damage;
    protected String predamage;
    protected String revealed;
    protected String frozen;
    protected String silenced;
    protected String stealth;

    protected CardDetails cardDetails;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Map<String, String> getUnknownTags() {
        return unknownTags;
    }

    public void setUnknownTags(Map<String, String> unknownTags) {
        this.unknownTags = unknownTags;
    }

    public String getCardid() {
        return cardDetails != null ? cardDetails.getId() : cardid;
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

    public String getArmor() {
        return armor;
    }

    public void setArmor(String armor) {
        this.armor = armor;
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
        return rarity != null ? rarity : "none";
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

    public String getPredamage() {
        return predamage;
    }

    public void setPredamage(String predamage) {
        this.predamage = predamage;
    }

    public String getRevealed() {
        return revealed;
    }

    public void setRevealed(String revealed) {
        this.revealed = revealed;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public String getSilenced() {
        return silenced;
    }

    public void setSilenced(String silenced) {
        this.silenced = silenced;
    }

    public String getStealth() {
        return stealth;
    }

    public void setStealth(String stealth) {
        this.stealth = stealth;
    }

    public CardDetails getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(CardDetails cardDetails) {
        this.cardDetails = cardDetails;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + entityId + '\'' +
                ", cardid='" + cardid + '\'' +
                ", name='" + (cardDetails != null ? cardDetails.getName() : "") +
                ", atk='" + atk + '\'' +
                ", health='" + health + '\'' +
                ", cost='" + cost + '\'' +
                ", cardtype='" + cardtype + '\'' +
                '}';
    }

    public String getCardDetailsName() {
        return cardDetails != null && cardDetails.getName() != null ? cardDetails.getName() : "a card ";
    }

    public String getCardDetailsId() {
        return cardDetails != null && cardDetails.getId() != null ? cardDetails.getId() : "cardback";
    }

    public String getCardDetailsRarity() {
        return cardDetails != null && cardDetails.getRarity() != null ? cardDetails.getRarity() : "none";
    }

    public int getCardDetailsCost() {
        return cardDetails != null ? cardDetails.getCost() : 0;
    }

    public int getCardDetailsHealth() {
        return cardDetails != null ? cardDetails.getHealth() : 0;
    }

    public int getCardDetailsAttack() {
        return cardDetails != null ? cardDetails.getAttack() : 0;
    }

    public boolean isSpell() {
        return Type.SPELL.eq(this.getCardtype()) || (this.getCardDetails() != null && "spell".equalsIgnoreCase(this.getCardDetails().getType()));
    }

    public boolean isMinion() {
        return Type.MINION.eq(this.getCardtype()) || (this.getCardDetails() != null && "minion".equalsIgnoreCase(this.getCardDetails().getType()));
    }

    public boolean isWeapon() {
        return Type.WEAPON.eq(this.getCardtype()) || (this.getCardDetails() != null && "weapon".equalsIgnoreCase(this.getCardDetails().getType()));
    }

    public boolean isEnchantment() {
        return Type.ENCHANTMENT.eq(this.getCardtype()) || (this.getCardDetails() != null && "enchantment".equalsIgnoreCase(this.getCardDetails().getType()));
    }

    public boolean isPlayer() {
        return Type.PLAYER.eq(this.getCardtype());
    }

    public boolean isGame() {
        return Type.GAME.eq(this.getCardtype());
    }

    public boolean isCard() {
        return !isPlayer() && !isGame();
    }

    public boolean isHero() {
        return Type.HERO.eq(this.getCardtype());
    }

    public boolean isHeroPower() {
        return Type.HERO_POWER.eq(this.getCardtype());
    }


    public static boolean isMinion(Card before, Card after) {
        return Card.Type.MINION.eq(before.getCardtype()) || (before.getCardDetails() != null && Type.MINION.eq(before.getCardDetails().getType())) || Card.Type.MINION.eq(after.getCardtype());
    }

    public static boolean isSpell(Card before, Card after) {
        return Card.Type.SPELL.eq(before.getCardtype()) || (before.getCardDetails() != null && Type.SPELL.eq(before.getCardDetails().getType())) || Card.Type.SPELL.eq(after.getCardtype());
    }

}
