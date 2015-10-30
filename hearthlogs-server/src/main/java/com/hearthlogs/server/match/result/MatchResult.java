package com.hearthlogs.server.match.result;

import com.hearthlogs.server.match.domain.Card;
import com.hearthlogs.server.match.domain.Player;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class MatchResult {

    private Player friendly;
    private Player opposing;
    private Player winner;
    private Player loser;
    private Player quitter;
    private Player first;

    private String rank;

    private Set<Card> friendlyStartingCards = new LinkedHashSet<>();
    private Set<Card> opposingStartingCards = new LinkedHashSet<>();

    private Set<Card> friendlyMulliganedCards = new LinkedHashSet<>();
    private Set<Card> opposingMulliganedCards = new LinkedHashSet<>();

    private String winnerClass; // the class (i.e. warrior, hunter, etc...)
    private String loserClass;

    private Set<Turn> turns = new LinkedHashSet<>();
    private Turn currentTurn;

    public void addFriendlyStartingCard(Card card) {
        friendlyStartingCards.add(card);
    }

    public void addOpposingStartingCard(Card card) {
        opposingStartingCards.add(card);
    }

    public void mulliganFriendlyCard(Card card) {
        friendlyMulliganedCards.add(card);
        friendlyStartingCards.remove(card);
    }

    public void mulliganOpposingCard(Card card) {
        opposingMulliganedCards.add(card);
        opposingStartingCards.remove(card);
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

    public Set<Turn> getTurns() {
        return turns;
    }

    public void setTurns(Set<Turn> turns) {
        this.turns = turns;
    }

    public void addTurn(Turn turn) {
        currentTurn = turn;
        this.turns.add(turn);
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setFriendlyStartingCards(Set<Card> friendlyStartingCards) {
        this.friendlyStartingCards = friendlyStartingCards;
    }

    public void setOpposingStartingCards(Set<Card> opposingStartingCards) {
        this.opposingStartingCards = opposingStartingCards;
    }

    public void setFriendlyMulliganedCards(Set<Card> friendlyMulliganedCards) {
        this.friendlyMulliganedCards = friendlyMulliganedCards;
    }

    public void setOpposingMulliganedCards(Set<Card> opposingMulliganedCards) {
        this.opposingMulliganedCards = opposingMulliganedCards;
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


    public Player getFriendly() {
        return friendly;
    }

    public void setFriendly(Player friendly) {
        this.friendly = friendly;
    }

    public Player getOpposing() {
        return opposing;
    }

    public void setOpposing(Player opposing) {
        this.opposing = opposing;
    }

    public long getFriendlyTotalMana() {
        return getMana(friendly, Turn::getManaGained);
    }

    public long getFriendlyManaUsed() {
        return getMana(friendly, Turn::getManaUsed);
    }

    public long getFriendlyManaSaved() {
        return getMana(friendly, Turn::getManaSaved);
    }

    public float getFriendlyManaEfficiency() {
        return (float) getFriendlyManaUsed() / (getFriendlyTotalMana() - getFriendlyManaSaved());
    }

    public long getOpposingTotalMana() {
        return getMana(opposing, Turn::getManaGained);
    }

    public long getOpposingManaUsed() {
        return getMana(opposing, Turn::getManaUsed);
    }

    public long getOpposingManaSaved() {
        return getMana(opposing, Turn::getManaSaved);
    }

    private long getMana(Player player, ToIntFunction<Turn> turnToIntFunction) {
        IntSummaryStatistics stats = turns.stream().filter(t -> t.getWhoseTurn() == player).collect(Collectors.summarizingInt(turnToIntFunction));
        return stats.getSum();

    }

    public float getOpposingManaEfficiency() {
        return (float) getOpposingManaUsed() / (getOpposingTotalMana() - getOpposingManaSaved());
    }


}
