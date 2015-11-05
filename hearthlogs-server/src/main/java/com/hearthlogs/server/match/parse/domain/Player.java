package com.hearthlogs.server.match.parse.domain;

import java.io.Serializable;

public class Player extends Entity implements Serializable {

    private static final long serialVersionUID = 1;

    public enum PlayState {
        WON,
        LOST,
        QUIT;

        public boolean eq(String playState) {
            return this.toString().equals(playState);
        }
    }

    public static final String DEALING = "DEALING";

    private String rank;
    private String gameAccountIdHi;
    private String gameAccountIdLo;
    private String name;
    private String timeout;
    private String playstate;
    private String currentPlayer;
    private String firstPlayer;
    private String heroEntity;
    private String maxhandsize;
    private String starthandsize;
    private String playerId;
    private String teamId;
    private String zone;
    private String controller;
    private String maxresources;
    private String cardtype;
    private String numTurnsLeft;
    private String numCardsDrawnThisTurn;
    private String mulliganState;
    private String numOptions;
    private String lastCardPlayed;
    private String comboActive;
    private String numOptionsPlayedThisTurn;
    private String currentSpellpower;
    private String numMinionsPlayerKilledThisTurn;
    private String goldRewardState;
    private String resources;
    private String resourcesUsed;
    private String tempResources;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getPlaystate() {
        return playstate;
    }

    public void setPlaystate(String playstate) {
        this.playstate = playstate;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getHeroEntity() {
        return heroEntity;
    }

    public void setHeroEntity(String heroEntity) {
        this.heroEntity = heroEntity;
    }

    public String getMaxhandsize() {
        return maxhandsize;
    }

    public void setMaxhandsize(String maxhandsize) {
        this.maxhandsize = maxhandsize;
    }

    public String getStarthandsize() {
        return starthandsize;
    }

    public void setStarthandsize(String starthandsize) {
        this.starthandsize = starthandsize;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
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

    public String getMaxresources() {
        return maxresources;
    }

    public void setMaxresources(String maxresources) {
        this.maxresources = maxresources;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getNumTurnsLeft() {
        return numTurnsLeft;
    }

    public void setNumTurnsLeft(String numTurnsLeft) {
        this.numTurnsLeft = numTurnsLeft;
    }

    public String getNumCardsDrawnThisTurn() {
        return numCardsDrawnThisTurn;
    }

    public void setNumCardsDrawnThisTurn(String numCardsDrawnThisTurn) {
        this.numCardsDrawnThisTurn = numCardsDrawnThisTurn;
    }

    public String getMulliganState() {
        return mulliganState;
    }

    public void setMulliganState(String mulliganState) {
        this.mulliganState = mulliganState;
    }

    public String getNumOptions() {
        return numOptions;
    }

    public void setNumOptions(String numOptions) {
        this.numOptions = numOptions;
    }

    public String getLastCardPlayed() {
        return lastCardPlayed;
    }

    public void setLastCardPlayed(String lastCardPlayed) {
        this.lastCardPlayed = lastCardPlayed;
    }

    public String getComboActive() {
        return comboActive;
    }

    public void setComboActive(String comboActive) {
        this.comboActive = comboActive;
    }

    public String getNumOptionsPlayedThisTurn() {
        return numOptionsPlayedThisTurn;
    }

    public void setNumOptionsPlayedThisTurn(String numOptionsPlayedThisTurn) {
        this.numOptionsPlayedThisTurn = numOptionsPlayedThisTurn;
    }

    public String getCurrentSpellpower() {
        return currentSpellpower;
    }

    public void setCurrentSpellpower(String currentSpellpower) {
        this.currentSpellpower = currentSpellpower;
    }

    public String getNumMinionsPlayerKilledThisTurn() {
        return numMinionsPlayerKilledThisTurn;
    }

    public void setNumMinionsPlayerKilledThisTurn(String numMinionsPlayerKilledThisTurn) {
        this.numMinionsPlayerKilledThisTurn = numMinionsPlayerKilledThisTurn;
    }

    public String getGoldRewardState() {
        return goldRewardState;
    }

    public void setGoldRewardState(String goldRewardState) {
        this.goldRewardState = goldRewardState;
    }

    public String getGameAccountIdHi() {
        return gameAccountIdHi;
    }

    public void setGameAccountIdHi(String gameAccountIdHi) {
        this.gameAccountIdHi = gameAccountIdHi;
    }

    public String getGameAccountIdLo() {
        return gameAccountIdLo;
    }

    public void setGameAccountIdLo(String gameAccountIdLo) {
        this.gameAccountIdLo = gameAccountIdLo;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getResourcesUsed() {
        return resourcesUsed;
    }

    public void setResourcesUsed(String resourcesUsed) {
        this.resourcesUsed = resourcesUsed;
    }

    public String getTempResources() {
        return tempResources;
    }

    public void setTempResources(String tempResources) {
        this.tempResources = tempResources;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", playerId='" + playerId + '\'' +
                '}';
    }
}