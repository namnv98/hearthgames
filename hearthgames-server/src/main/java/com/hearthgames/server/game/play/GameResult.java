package com.hearthgames.server.game.play;

import com.hearthgames.server.game.parse.domain.*;
import com.hearthgames.server.game.play.domain.*;

import java.util.*;

public class GameResult {

    private List<Player> winners = new ArrayList<>();
    private List<Player> losers = new ArrayList<>();
    private Player quitter;
    private Player first;

    private Integer rank;

    private Set<Card> friendlyStartingCards = new LinkedHashSet<>();
    private Set<Card> opposingStartingCards = new LinkedHashSet<>();

    private Set<Card> friendlyMulliganedCards = new LinkedHashSet<>();
    private Set<Card> opposingMulliganedCards = new LinkedHashSet<>();

    private Set<Card> friendlyDeckCards = new LinkedHashSet<>();
    private Set<Card> opposingDeckCards = new LinkedHashSet<>();

    private List<Turn> turns = new ArrayList<>();
    private Turn currentTurn;
    private int turnNumber;

    private List<String> actionLogs = new ArrayList<>();

    public void addActionLog(String log) {
        //System.out.println(log);
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

    public List<Player> getWinners() {
        return winners;
    }

    public void setWinners(List<Player> winners) {
        this.winners = winners;
    }

    public List<Player> getLosers() {
        return losers;
    }

    public void setLosers(List<Player> losers) {
        this.losers = losers;
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

    public List<Turn> getTurns() {
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

    public void setTurns(List<Turn> turns) {
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

    public String getWinner() {
        if (winners.size() > 1) {
            return "Both";
        } else if (winners.size() == 0) {
            return "None";
        } else {
           return winners.get(0).getName();
        }
    }

    public String getLoser() {
        if (losers.size() > 1) {
            return "Both";
        } else if (losers.size() == 0) {
            return "None";
        } else {
            return losers.get(0).getName();
        }
    }

    public String getWinnerClass() {
        if (winners.size() > 1) {
            return "N/A";
        } else if (winners.size() == 0){
            return "";
        } else {
            return winners.get(0).getPlayerClass() == null ? "unknown" : winners.get(0).getPlayerClass();
        }
    }

    public String getLoserClass() {
        if (losers.size() > 1) {
            return "N/A";
        } else if (losers.size() == 0){
            return "";
        } else {
            return losers.get(0).getPlayerClass() == null ? "unknown" : losers.get(0).getPlayerClass();
        }
    }

}
