package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;
import com.hearthlogs.server.match.domain.Zone;

public class ZoneChange {

    private Card card;
    private Zone fromZone;
    private Zone toZone;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
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
}
