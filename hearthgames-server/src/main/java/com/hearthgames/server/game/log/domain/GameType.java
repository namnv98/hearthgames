package com.hearthgames.server.game.log.domain;

public enum GameType {
    UNKNOWN(0),
    CASUAL(1),
    RANKED(2),
    ARENA(3),
    ADVENTURE(4),
    TAVERN_BRAWL(5),
    FRIENDLY_CHALLENGE(6);

    private int type;
    GameType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static GameType getGameType(int gameType) {
        if (gameType == CASUAL.getType()) return CASUAL;
        if (gameType == RANKED.getType()) return RANKED;
        if (gameType == ARENA.getType()) return ARENA;
        if (gameType == ADVENTURE.getType()) return ADVENTURE;
        if (gameType == TAVERN_BRAWL.getType()) return TAVERN_BRAWL;
        if (gameType == FRIENDLY_CHALLENGE.getType()) return FRIENDLY_CHALLENGE;
        return UNKNOWN;
    }
}
