package com.hearthgames.server.game.play.domain.board;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.play.domain.board.json.HeroSerializer;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(using = HeroSerializer.class)
public class Hero {

    private String cardId;
    private String id;
    private Integer health;
    private Boolean damaged;
    private Integer armor;
    private Integer attack;
    private Integer mana;
    private Integer manaTotal;

    private String powerId;
    private Boolean powerUsed;

    private List<CardInHand> cardsInHand = new ArrayList<>();
    private List<CardInSecret> cardsInSecret = new ArrayList<>();
    private List<MinionInPlay> minionsInPlay = new ArrayList<>();
    private Weapon weapon;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Boolean getDamaged() {
        return damaged;
    }

    public void setDamaged(Boolean damaged) {
        this.damaged = damaged;
    }

    public Integer getArmor() {
        return armor;
    }

    public void setArmor(Integer armor) {
        this.armor = armor;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getMana() {
        return mana;
    }

    public void setMana(Integer mana) {
        this.mana = mana;
    }

    public Integer getManaTotal() {
        return manaTotal;
    }

    public void setManaTotal(Integer manaTotal) {
        this.manaTotal = manaTotal;
    }

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }

    public Boolean getPowerUsed() {
        return powerUsed;
    }

    public void setPowerUsed(Boolean powerUsed) {
        this.powerUsed = powerUsed;
    }

    public List<CardInHand> getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(List<CardInHand> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public List<CardInSecret> getCardsInSecret() {
        return cardsInSecret;
    }

    public void setCardsInSecret(List<CardInSecret> cardsInSecret) {
        this.cardsInSecret = cardsInSecret;
    }

    public List<MinionInPlay> getMinionsInPlay() {
        return minionsInPlay;
    }

    public void setMinionsInPlay(List<MinionInPlay> minionsInPlay) {
        this.minionsInPlay = minionsInPlay;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
