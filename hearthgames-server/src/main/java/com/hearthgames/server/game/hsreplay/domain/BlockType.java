package com.hearthgames.server.game.hsreplay.domain;

public enum  BlockType {

    UNKNOWN(0),
    ATTACK(1),
    JOUST(2),
    POWER(3),
    TRIGGER(5),
    DEATHS(6),
    PLAY(7),
    FATIGUE(8),

    // Removed
    SCRIPT(4),
    ACTION(99),

    // Renamed
    CONTINUOUS(2);

    private int type;

    BlockType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static BlockType getBlockTypeByValue(String value) {
        int val = 0;
        try {
            val = Integer.parseInt(value);
        } catch (NumberFormatException e) {
        }
        for (BlockType blockType: BlockType.values()) {
            if (blockType.getType() == val) {
                return blockType;
            }
        }
        return BlockType.UNKNOWN;
    }

}
