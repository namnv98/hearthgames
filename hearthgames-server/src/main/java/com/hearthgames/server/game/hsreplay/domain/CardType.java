package com.hearthgames.server.game.hsreplay.domain;

public enum CardType {

    INVALID(0),
    GAME(1),
    PLAYER(2),
    HERO(3),
    MINION(4),
    SPELL(5),
    ENCHANTMENT(6),
    WEAPON(7),
    ITEM(8),
    TOKEN(9),
    HERO_POWER(10),

    // Renamed
    ABILITY(5);

    private int type;

    CardType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static CardType getCardTypeByValue(String value) {
        int val = 0;
        try {
            val = Integer.parseInt(value);
        } catch (NumberFormatException e) {
        }
        for (CardType cardType: CardType.values()) {
            if (cardType.getType() == val) {
                return cardType;
            }
        }
        return INVALID;
    }
}
