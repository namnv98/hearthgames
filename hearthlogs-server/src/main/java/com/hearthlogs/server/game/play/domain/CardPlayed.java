package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.parse.domain.Zone;

import java.io.Serializable;

public class CardPlayed implements Action, Serializable {

    private Zone fromZone;
    private Zone toZone;
    private Player beneficiary;
    private Card card;

    public CardPlayed(Zone fromZone, Zone toZone, Player beneficiary, Card card) {
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.beneficiary = beneficiary;
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Player getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Player beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Zone getFromZone() {
        return fromZone;
    }

    public void setFromZone(Zone fromZone) {
        this.fromZone = fromZone;
    }

    public Zone getToZone() {
        return toZone;
    }

    public void setToZone(Zone toZone) {
        this.toZone = toZone;
    }

    @Override
    public int getType() {
        return 7;
    }
}
