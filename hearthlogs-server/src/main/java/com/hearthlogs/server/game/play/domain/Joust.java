package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class Joust implements Action, Serializable {

    private Player friendly;
    private Player opposing;
    private String friendlyCardId;
    private String oppsosingCardId;
    private Card card;
    private boolean winner;

    public Joust(Player friendly, Player opposing, String friendlyCardId, String oppsosingCardId, Card card, boolean winner) {
        this.friendly = friendly;
        this.opposing = opposing;
        this.friendlyCardId = friendlyCardId;
        this.oppsosingCardId = oppsosingCardId;
        this.card = card;
        this.winner = winner;
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

    public String getFriendlyCardId() {
        return friendlyCardId;
    }

    public void setFriendlyCardId(String friendlyCardId) {
        this.friendlyCardId = friendlyCardId;
    }

    public String getOppsosingCardId() {
        return oppsosingCardId;
    }

    public void setOppsosingCardId(String oppsosingCardId) {
        this.oppsosingCardId = oppsosingCardId;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    @Override
    public int getType() {
        return 15;
    }
}
