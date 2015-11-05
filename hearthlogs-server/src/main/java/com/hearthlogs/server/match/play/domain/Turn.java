package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Player;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Turn implements Serializable {

    private int turnNumber;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private Player whoseTurn;

    private int tempManaUsed; // this is the amount that was JUST used and should be zero'd out after read

    private Set<Card> friendlyCardsPutInPlay = new LinkedHashSet<>();
    private Set<Card> friendlyCardsRemovedFromPlay = new LinkedHashSet<>();
    private Set<Card> opposingCardsPutInPlay = new LinkedHashSet<>();
    private Set<Card> opposingCardsRemovedFromPlay = new LinkedHashSet<>();

    private Set<Card> friendlyCardsInPlay = new HashSet<>();
    private Set<Card> opposingCardsInPlay = new HashSet<>();

    private List<Action> actions = new ArrayList<>();

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public Turn(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
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

    public Set<Card> getFriendlyCardsPutInPlay() {
        return friendlyCardsPutInPlay;
    }

    public void setFriendlyCardsPutInPlay(Set<Card> friendlyCardsPutInPlay) {
        this.friendlyCardsPutInPlay = friendlyCardsPutInPlay;
    }

    public Set<Card> getFriendlyCardsRemovedFromPlay() {
        return friendlyCardsRemovedFromPlay;
    }

    public void setFriendlyCardsRemovedFromPlay(Set<Card> friendlyCardsRemovedFromPlay) {
        this.friendlyCardsRemovedFromPlay = friendlyCardsRemovedFromPlay;
    }

    public Set<Card> getOpposingCardsPutInPlay() {
        return opposingCardsPutInPlay;
    }

    public void setOpposingCardsPutInPlay(Set<Card> opposingCardsPutInPlay) {
        this.opposingCardsPutInPlay = opposingCardsPutInPlay;
    }

    public Set<Card> getOpposingCardsRemovedFromPlay() {
        return opposingCardsRemovedFromPlay;
    }

    public void setOpposingCardsRemovedFromPlay(Set<Card> opposingCardsRemovedFromPlay) {
        this.opposingCardsRemovedFromPlay = opposingCardsRemovedFromPlay;
    }


    public Set<Card> getFriendlyCardsInPlay() {
        return friendlyCardsInPlay;
    }

    public void setFriendlyCardsInPlay(Set<Card> friendlyCardsInPlay) {
        this.friendlyCardsInPlay = friendlyCardsInPlay;
    }

    public Set<Card> getOpposingCardsInPlay() {
        return opposingCardsInPlay;
    }

    public void setOpposingCardsInPlay(Set<Card> opposingCardsInPlay) {
        this.opposingCardsInPlay = opposingCardsInPlay;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
