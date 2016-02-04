package com.hearthgames.server.game.hsreplay.domain;

public enum Zone {

    INVALID(0),
    PLAY(1),
    DECK(2),
    HAND(3),
    GRAVEYARD(4),
    REMOVEDFROMGAME(5),
    SETASIDE(6),
    SECRET(7),

    // Not public
    DISCARD(-2);

    private int zone;

    Zone(int zone) {
        this.zone = zone;
    }

    public int getZone() {
        return zone;
    }

    public static Zone getZoneByValue(String value) {
        int val = 0;
        try {
            val = Integer.parseInt(value);
        } catch (NumberFormatException e) {
        }
        for (Zone zone: Zone.values()) {
            if (zone.getZone() == val) {
                return zone;
            }
        }
        return INVALID;
    }

}
