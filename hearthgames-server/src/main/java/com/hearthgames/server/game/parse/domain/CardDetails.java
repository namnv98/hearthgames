package com.hearthgames.server.game.parse.domain;

import java.io.Serializable;
import java.util.List;

public class CardDetails implements Serializable {

    private static final long serialVersionUID = 1;

    private String id;
    private String name;
    private String type;
    private String faction;
    private String rarity;
    private int cost;
    private int attack;
    private int health;
    private String text;
    private String textInPlay;
    private String flavor;
    private String artist;
    private Boolean collectible;
    private List<String> mechanics;
    private String playerClass;
    private int durability;
    private String race;
    private String inPlayText;
    private String elite;
    private String set;
    private String howToEarn;
    private String howToEarnGolden;
    private List<String> entourage;
    private PlayRequirements playRequirements;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextInPlay() {
        return textInPlay;
    }

    public void setTextInPlay(String textInPlay) {
        this.textInPlay = textInPlay;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Boolean getCollectible() {
        return collectible;
    }

    public void setCollectible(Boolean collectible) {
        this.collectible = collectible;
    }

    public List<String> getMechanics() {
        return mechanics;
    }

    public void setMechanics(List<String> mechanics) {
        this.mechanics = mechanics;
    }

    public String getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(String playerClass) {
        this.playerClass = playerClass;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getInPlayText() {
        return inPlayText;
    }

    public void setInPlayText(String inPlayText) {
        this.inPlayText = inPlayText;
    }

    public String getElite() {
        return elite;
    }

    public void setElite(String elite) {
        this.elite = elite;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getHowToEarn() {
        return howToEarn;
    }

    public void setHowToEarn(String howToEarn) {
        this.howToEarn = howToEarn;
    }

    public String getHowToEarnGolden() {
        return howToEarnGolden;
    }

    public void setHowToEarnGolden(String howToEarnGolden) {
        this.howToEarnGolden = howToEarnGolden;
    }

    public List<String> getEntourage() {
        return entourage;
    }

    public void setEntourage(List<String> entourage) {
        this.entourage = entourage;
    }

    public PlayRequirements getPlayRequirements() {
        return playRequirements;
    }

    public void setPlayRequirements(PlayRequirements playRequirements) {
        this.playRequirements = playRequirements;
    }
}
