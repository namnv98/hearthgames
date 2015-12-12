package com.hearthgames.server.game.analysis.domain;

import com.hearthgames.server.game.play.domain.board.Board;

import java.util.ArrayList;
import java.util.List;

public class TurnInfo {

    private String turnNumber;
    private String whoseTurn;
    private String turnClass;

    private List<Board> boards = new ArrayList<>();

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

    public String getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(String turnNumber) {
        this.turnNumber = turnNumber;
    }

    public String getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(String whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public String getTurnClass() {
        return turnClass;
    }

    public void setTurnClass(String turnClass) {
        this.turnClass = turnClass;
    }
}
