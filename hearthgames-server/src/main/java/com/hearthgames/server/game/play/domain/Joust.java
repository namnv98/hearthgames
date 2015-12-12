package com.hearthgames.server.game.play.domain;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;

import java.io.Serializable;

public class Joust implements Action, Serializable {

    private Player friendly;
    private Player opposing;
    private Card friendlyJouster;
    private Card oppsosingJouster;
    private Card card;
    private boolean winner;

    public Joust(Player friendly, Player opposing, Card friendlyJouster, Card oppsosingJouster, Card card, boolean winner) {
        this.friendly = friendly;
        this.opposing = opposing;
        this.friendlyJouster = friendlyJouster;
        this.oppsosingJouster = oppsosingJouster;
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

    public Card getFriendlyJouster() {
        return friendlyJouster;
    }

    public void setFriendlyJouster(Card friendlyJouster) {
        this.friendlyJouster = friendlyJouster;
    }

    public Card getOppsosingJouster() {
        return oppsosingJouster;
    }

    public void setOppsosingJouster(Card oppsosingJouster) {
        this.oppsosingJouster = oppsosingJouster;
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
    public String toString() {
        return friendlyJouster.getName() + " is jousting : " + oppsosingJouster.getName() + (winner ? friendlyJouster.getName() + " has won the joust!" : friendlyJouster.getName() + " has lost the joust!");
    }
}
