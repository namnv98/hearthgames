package com.hearthlogs.server.game.play.domain;

public class EndOfTurn implements Action {
    @Override
    public int getType() {
        return 10;
    }
}
