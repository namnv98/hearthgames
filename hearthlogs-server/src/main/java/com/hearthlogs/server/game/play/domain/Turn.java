package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Turn implements Serializable {

    private int turnNumber;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private Player whoseTurn;

    private int tempManaUsed; // this is the amount that was JUST used and should be zero'd out after read

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

    public int getManaLost() {
        int sum = 0;
        for (Action a: actions) {
            if (a instanceof ManaLost) {
                sum += ((ManaLost) a).getManaLost();
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

    public Board findFirstBoard() {
        for (Action action: getActions()) {
            if (action instanceof Board) {
                return (Board) action;
            }
        }
        return null;
    }

    public Board findLastBoard() {
        Board board = null;
        for (Action action: getActions()) {
            if (action instanceof Board) {
                board = (Board) action;
            }
        }
        return board;
    }
}
