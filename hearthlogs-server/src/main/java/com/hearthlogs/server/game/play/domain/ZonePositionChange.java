package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Zone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZonePositionChange implements Action, Serializable {

    private Card card;
    private Zone zone;
    private int position;

    private List<ZonePositionChange> zonePositionChanges = new ArrayList<>();

    public ZonePositionChange(Card card, Zone zone, int position) {
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

    public void addZonePositionChange(ZonePositionChange change) {
        zonePositionChanges.add(change);
    }

    public List<ZonePositionChange> getZonePositionChanges() {
        return zonePositionChanges;
    }
}
