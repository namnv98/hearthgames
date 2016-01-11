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

    private List<Turn> turns = new ArrayList<>();
    private Turn currentTurn;
    private int turnNumber;

    private List<String> actionLogs = new ArrayList<>();

    public void addActionLog(String log) {
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

    public Action getLastAction() {
        if (turns != null && turns.size() > 0) {
            Turn turn = turns.get(turns.size()-1);
            if (turn.getActions() != null && turn.getActions().size() > 0) {
                return turn.getActions().get(turn.getActions().size()-1);
            } else if (turns.size() > 1) { // if the latest turn doesnt have any action go back to the previous turn and get the last action
                turn = turns.get(turns.size()-2);
                if (turn.getActions() != null && turn.getActions().size() > 0) {
                    return turn.getActions().get(turn.getActions().size()-1);
                }
            }
        }
        return null;
    }
}
