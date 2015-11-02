package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Zone;

import java.io.Serializable;

public class ZonePositionChange implements Action, Serializable {

    private Card card;
    private Zone zone;
    private int position;

    protected ZonePositionChange(Card card, Zone zone, int position) {
        this.card = card;
        this.zone = zone;
        this.position = position;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
