package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.json.JoustSerializer;

import java.io.Serializable;

@JsonSerialize(using = JoustSerializer.class)
public class Joust implements Action, Serializable {

    private Player friendly;
    private Player opposing;
    private Card friendlyJouster;
    private Card opposingJouster;
    private Card card;
    private boolean winner;

    public Joust(Player friendly, Player opposing, Card friendlyJouster, Card opposingJouster, Card card, boolean winner) {
        this.friendly = friendly;
        this.opposing = opposing;
        this.friendlyJouster = friendlyJouster;
        this.opposingJouster = opposingJouster;
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

    public Card getOpposingJouster() {
        return opposingJouster;
    }

    public void setOpposingJouster(Card opposingJouster) {
        this.opposingJouster = opposingJouster;
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
        return friendlyJouster.getName() + " is jousting : " + opposingJouster.getName() + (winner ? friendlyJouster.getName() + " has won the joust!" : friendlyJouster.getName() + " has lost the joust!");
    }
}
