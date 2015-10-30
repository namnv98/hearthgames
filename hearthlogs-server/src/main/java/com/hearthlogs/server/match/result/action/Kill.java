package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;

public class Kill implements Action {

    private Card killer;
    private Card killed;

    public Card getKiller() {
        return killer;
    }

    public void setKiller(Card killer) {
        this.killer = killer;
    }

    public Card getKilled() {
        return killed;
    }

    public void setKilled(Card killed) {
        this.killed = killed;
    }
}
