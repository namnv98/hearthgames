package com.hearthgames.server.game.play.domain;

public class LoggingAction implements Action {

    private String msg;

    public LoggingAction(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
